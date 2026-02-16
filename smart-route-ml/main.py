from fastapi import FastAPI
from pydantic import BaseModel
from typing import List
import pandas as pd
from sklearn.linear_model import LogisticRegression
from sklearn.preprocessing import OneHotEncoder
from sklearn.compose import ColumnTransformer
from sklearn.pipeline import Pipeline

app = FastAPI()

# Global model (stored in memory)
model = None


# -------- DATA STRUCTURES --------

class TransactionRecord(BaseModel):
    route: str
    amount: float
    latency: int
    result: str  # SUCCESS or DECLINE


class TrainRequest(BaseModel):
    transactions: List[TransactionRecord]


class PredictRequest(BaseModel):
    route: str
    amount: float
    latency: int


#-------------TEST ML---------------
model_trained = False

@app.get("/status")
def status():
    return {"model_trained": model_trained}



# -------- Training Endpoint --------

@app.post("/train")
def train_model(request: TrainRequest):

    global model

    if len(request.transactions) < 10:
        return {"error": "Not enough data to train"}

    data = pd.DataFrame([t.dict() for t in request.transactions])

    # Convert SUCCESS/DECLINE → 1/0
    data["target"] = data["result"].apply(
        lambda x: 1 if x == "SUCCESS" else 0
    )

    X = data[["route", "amount", "latency"]]
    y = data["target"]

    # One-hot encode route
    preprocessor = ColumnTransformer(
        transformers=[
            ("route", OneHotEncoder(handle_unknown="ignore"), ["route"])
        ],
        remainder="passthrough"
    )

    model = Pipeline(
        steps=[
            ("preprocessor", preprocessor),
            ("classifier", LogisticRegression())
        ]
    )

    model.fit(X, y)

    accuracy = model.score(X, y)
    
    global model_trained
    model_trained = True

    return {
        "message": "Model trained successfully",
        "training_accuracy": round(float(accuracy), 4)
    }


# -------- Predict Endpoint --------

@app.post("/predict")
def predict(request: PredictRequest):

    global model

    if model is None:
        return {"error": "Model not trained yet"}

    input_df = pd.DataFrame([{
        "route": request.route,
        "amount": request.amount,
        "latency": request.latency
    }])

    probability = model.predict_proba(input_df)[0][1]

    return {
        "success_probability": round(float(probability), 4)
    }
    

