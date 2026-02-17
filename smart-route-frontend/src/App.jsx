import { useState } from "react";
import Dashboard from "./pages/Dashboard";
import Analytics from "./pages/Analytics";
import Admin from "./pages/Admin";
import { useEffect } from "react";
import { getRoutes, checkMLStatus } from "./services/api";
export default function App() {
  const [page, setPage] = useState("dashboard");

  useEffect(() => {
  async function init() {
    const routes = await getRoutes();
    setRoutes(routes);

    const ml = await checkMLStatus();
    setMlStatus(ml.ml_ready ? "Trained" : "Not Trained");
  }
  init();
}, []);

  // 🔑 GLOBAL SHARED STATE
  const [comparisonData, setComparisonData] = useState(null);
  const [routes, setRoutes] = useState([]);
  const [mlStatus, setMlStatus] = useState("Unknown");

  return (
    <div className="min-h-screen bg-[#020617] text-white">

      {/* NAV */}
      <div className="flex gap-6 p-4 bg-black border-b border-gray-800">
        <Nav label="Dashboard" onClick={() => setPage("dashboard")} />
        <Nav label="Analytics" onClick={() => setPage("analytics")} />
        <Nav label="Admin" onClick={() => setPage("admin")} />
      </div>

      {page === "dashboard" && (
        <Dashboard
          routes={routes}
          setRoutes={setRoutes}
          comparisonData={comparisonData}
          mlStatus={mlStatus}
        />
      )}

      {page === "analytics" && (
        <Analytics
          comparisonData={comparisonData}
          setComparisonData={setComparisonData}
        />
      )}

      {page === "admin" && (
        <Admin
          setRoutes={setRoutes}
          mlStatus={mlStatus}
          setMlStatus={setMlStatus}
        />
      )}
    </div>
  );
}

function Nav({ label, onClick }) {
  return (
    <button
      onClick={onClick}
      className="px-4 py-2 rounded bg-gray-800 hover:bg-green-500 hover:text-black transition"
    >
      {label}
    </button>
  );
}