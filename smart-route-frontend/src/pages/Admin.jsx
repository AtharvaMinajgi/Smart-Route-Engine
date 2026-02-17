import { useEffect, useState } from "react";
import {
  getRoutes,
  updateRoute,
  resetBandit,
  trainML,
  checkMLStatus,
} from "../services/api";

import AdminRouteEditor from "../components/AdminRouteEditor";

export default function Admin() {
  const [routes, setRoutes] = useState([]);
  const [selectedRoute, setSelectedRoute] = useState(null);

  const [mlStatus, setMlStatus] = useState("Loading...");
  const [mlLoading, setMlLoading] = useState(false);
  const [banditLoading, setBanditLoading] = useState(false);
  const [mlResult, setMlResult] = useState(null);

  /* ---------- INITIAL LOAD ---------- */
  useEffect(() => {
    refreshRoutes();
    refreshMLStatus();
  }, []);

  const refreshRoutes = async () => {
    const data = await getRoutes();
    setRoutes(data);
    if (!selectedRoute && data.length > 0) {
      setSelectedRoute(data[0]);
    }
  };

  const refreshMLStatus = async () => {
    const res = await checkMLStatus();
    setMlStatus(res.ml_ready ? "Trained" : "Not Trained");
  };

  /* ---------- ACTIONS ---------- */
  const handleResetBandit = async () => {
    setBanditLoading(true);
    await resetBandit();
    setTimeout(() => setBanditLoading(false), 600);
  };

  const handleTrainML = async () => {
    setMlLoading(true);
    setMlResult(null);
    const res = await trainML();
    setMlResult(res);
    await refreshMLStatus();
    setMlLoading(false);
  };

  const handleSaveRoute = async (updated) => {
    await updateRoute(updated.id, updated);
    await refreshRoutes(); // 🔥 THIS FIXES AXIS-VISA BUG
  };

  return (
    <div className="min-h-screen bg-[#020617] text-white p-8 space-y-8">

      <h1 className="text-3xl font-bold text-green-400">
        Admin Control Panel
      </h1>

      {/* ROUTE SELECTOR */}
      <div className="flex gap-4 items-center">
        <label className="text-gray-400">Select Route:</label>
        <select
          className="bg-gray-800 px-4 py-2 rounded"
          value={selectedRoute?.id || ""}
          onChange={(e) =>
            setSelectedRoute(
              routes.find((r) => r.id === Number(e.target.value))
            )
          }
        >
          {routes.map((r) => (
            <option key={r.id} value={r.id}>
              {r.name}
            </option>
          ))}
        </select>
      </div>

      {/* ROUTE EDITOR */}
      {selectedRoute && (
        <AdminRouteEditor
          route={selectedRoute}
          onSave={handleSaveRoute}
        />
      )}

      {/* SYSTEM CONTROLS */}
      <div className="bg-[#0f172a] p-6 rounded-xl space-y-4">
        <h2 className="text-xl font-semibold text-green-400">
          System Controls
        </h2>

        <div className="flex gap-4 items-center">
          <button
            onClick={handleResetBandit}
            disabled={banditLoading}
            className={`px-4 py-2 rounded transition-all
              ${
                banditLoading
                  ? "bg-gray-600"
                  : "bg-red-600 hover:scale-105"
              }`}
          >
            {banditLoading ? "Resetting..." : "Reset Bandit"}
          </button>

          <button
            onClick={handleTrainML}
            disabled={mlLoading}
            className={`px-4 py-2 rounded transition-all
              ${
                mlLoading
                  ? "bg-gray-600"
                  : "bg-blue-600 hover:scale-105"
              }`}
          >
            {mlLoading ? "Training..." : "Train ML"}
          </button>

          <span className="ml-4 text-green-400">
            ML Status: {mlStatus}
          </span>
        </div>

        {mlResult && (
          <div className="bg-black p-4 rounded text-sm text-green-400">
            Training Accuracy: {(mlResult.training_accuracy * 100).toFixed(2)}%
          </div>
        )}
      </div>
    </div>
  );
}