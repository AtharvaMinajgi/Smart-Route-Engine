import { useState } from "react";
import { runTransaction, fetchRecentTransactions, getRoutes } from "../services/api";
import { useGlobal } from "../context/GlobalContext";

export default function TransactionForm() {
  const [amount, setAmount] = useState("");
  const [mode, setMode] = useState("INTELLIGENT");
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  const {
    setRecentTransactions,
    setRoutes,
  } = useGlobal();

  const handleRun = async () => {
    if (!amount) return;

    setLoading(true);
    try {
      // 1️⃣ Run transaction
      const res = await runTransaction(amount, mode);
      setResult(res);

      // 2️⃣ Refresh recent transactions from backend
      const recent = await fetchRecentTransactions();
      setRecentTransactions(recent);

      // 3️⃣ Refresh routes (to reflect degradation / stats)
      const routes = await getRoutes();
      setRoutes(routes);

    } catch (err) {
      console.error("Transaction failed", err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-gray-900 p-6 rounded-xl space-y-4">
      <h2 className="text-xl font-semibold text-green-400">
        Initiate Transaction
      </h2>

      <div className="flex gap-4">
        <input
          type="number"
          className="bg-gray-800 p-2 rounded w-full"
          placeholder="Amount ₹"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
        />

        <select
          className="bg-gray-800 p-2 rounded"
          value={mode}
          onChange={(e) => setMode(e.target.value)}
        >
          <option value="STATIC">STATIC</option>
          <option value="INTELLIGENT">INTELLIGENT</option>
        </select>

        <button
          onClick={handleRun}
          disabled={loading}
          className={`px-6 py-2 rounded transition
            ${loading
              ? "bg-gray-600 cursor-not-allowed"
              : "bg-green-500 hover:scale-105 active:scale-95"}
          `}
        >
          {loading ? "Running..." : "Run"}
        </button>
      </div>

      {/* RESULT (LIVE RESPONSE) */}
      {result && (
        <div className="bg-black p-4 rounded text-sm space-y-1">
          <p><b>Route:</b> {result.selectedRoute}</p>
          <p><b>Result:</b> {result.result}</p>
          <p><b>Latency:</b> {result.latencyMs} ms</p>
          <p><b>ML Probability:</b> {(result.mlProbability * 100).toFixed(2)}%</p>
          <p><b>Bandit Score:</b> {(result.banditScore * 100).toFixed(2)}%</p>
        </div>
      )}
    </div>
  );
}