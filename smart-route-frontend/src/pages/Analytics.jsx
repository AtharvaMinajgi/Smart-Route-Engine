// import { useState } from "react";
// import { runComparison } from "../services/api";
// import ChartPanel from "../components/ChartPanel";

// export default function Analytics() {
//   const [count, setCount] = useState(500);
//   const [data, setData] = useState(null);
//   const [loading, setLoading] = useState(false);

//   const handleRun = async () => {
//     setLoading(true);
//     setData(null);
//     const res = await runComparison(count);
//     setData(res);
//     setLoading(false);
//   };

//   return (
//     <div className="p-8 text-white">
//       <h1 className="text-3xl font-bold text-green-400 mb-6">
//         Analytics & Comparison
//       </h1>

//       <div className="flex gap-4 mb-6">
//         <input
//           type="number"
//           value={count}
//           onChange={(e) => setCount(e.target.value)}
//           className="bg-gray-800 p-2 rounded"
//         />

//         <button
//           onClick={handleRun}
//           disabled={loading}
//           className={`px-6 py-2 rounded ${
//             loading
//               ? "bg-gray-600"
//               : "bg-green-500 hover:scale-105"
//           }`}
//         >
//           {loading ? "Running..." : "Run Comparison"}
//         </button>
//       </div>

//       {data && <ChartPanel data={data} />}
//     </div>
//   );
// }

import { useState } from "react";
import { runComparison } from "../services/api";
import ChartPanel from "../components/ChartPanel";

export default function Analytics() {
  const [count, setCount] = useState(500);
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleRun = async () => {
    setLoading(true);
    setData(null);

    try {
      const res = await runComparison(count);
      setData(res);
    } catch (err) {
      console.error("Comparison failed", err);
      alert("Failed to run comparison");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-[#020617] text-white p-8 space-y-8">
      <h1 className="text-3xl font-bold text-green-400">
        Analytics & Route Comparison
      </h1>

      {/* CONTROLS */}
      <div className="flex items-center gap-4">
        <input
          type="number"
          value={count}
          onChange={(e) => setCount(Number(e.target.value))}
          className="bg-gray-800 p-2 rounded w-40"
          min={100}
        />

        <button
          onClick={handleRun}
          disabled={loading}
          className={`px-6 py-2 rounded transition-all
            ${
              loading
                ? "bg-gray-600 cursor-not-allowed"
                : "bg-green-500 hover:scale-105"
            }`}
        >
          {loading ? "Running..." : "Run Comparison"}
        </button>
      </div>

      {/* RESULT SUMMARY */}
      {data && (
        <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
          <Stat title="Static Success" value={`${data.staticSuccessRate * 100}%`} />
          <Stat title="AI Success" value={`${data.intelligentSuccessRate * 100}%`} highlight />
          <Stat title="Static Latency" value={`${data.staticAvgLatency} ms`} />
          <Stat title="AI Latency" value={`${data.intelligentAvgLatency} ms`} highlight />
        </div>
      )}

      {/* CHARTS */}
      {data && <ChartPanel data={data} />}
    </div>
  );
}

function Stat({ title, value, highlight }) {
  return (
    <div className="bg-gray-900 p-4 rounded">
      <p className="text-gray-400 text-sm">{title}</p>
      <p className={`text-2xl font-bold ${highlight ? "text-green-400" : ""}`}>
        {value}
      </p>
    </div>
  );
}