import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
  Legend,
  LineChart,
  Line,
} from "recharts";

export default function ChartPanel({ data }) {
  if (!data) return null;

  /* ---------- CHART DATA ---------- */
  const successData = [
    {
      name: "Static",
      Success: data.staticSuccessRate * 100,
    },
    {
      name: "Intelligent",
      Success: data.intelligentSuccessRate * 100,
    },
  ];

  const latencyData = [
    {
      name: "Static",
      Latency: data.staticAvgLatency,
    },
    {
      name: "Intelligent",
      Latency: data.intelligentAvgLatency,
    },
  ];

  return (
    <div className="grid grid-cols-1 lg:grid-cols-2 gap-8 mt-8">

      {/* SUCCESS RATE BAR CHART */}
      <div className="bg-[#0f172a] p-6 rounded-xl shadow">
        <h3 className="text-lg font-semibold text-green-400 mb-4">
          Success Rate Comparison (%)
        </h3>

        <ResponsiveContainer width="100%" height={300}>
          <BarChart data={successData}>
            <XAxis dataKey="name" stroke="#94a3b8" />
            <YAxis stroke="#94a3b8" />
            <Tooltip />
            <Legend />
            <Bar dataKey="Success" fill="#22c55e" radius={[6, 6, 0, 0]} />
          </BarChart>
        </ResponsiveContainer>
      </div>

      {/* LATENCY LINE CHART */}
      <div className="bg-[#0f172a] p-6 rounded-xl shadow">
        <h3 className="text-lg font-semibold text-green-400 mb-4">
          Average Latency (ms)
        </h3>

        <ResponsiveContainer width="100%" height={300}>
          <LineChart data={latencyData}>
            <XAxis dataKey="name" stroke="#94a3b8" />
            <YAxis stroke="#94a3b8" />
            <Tooltip />
            <Legend />
            <Line
              type="monotone"
              dataKey="Latency"
              stroke="#38bdf8"
              strokeWidth={3}
              dot={{ r: 6 }}
            />
          </LineChart>
        </ResponsiveContainer>
      </div>

      {/* IMPROVEMENT SUMMARY */}
      <div className="lg:col-span-2 bg-[#020617] border border-green-500/30 p-5 rounded-xl text-center">
        <p className="text-green-400 text-lg font-semibold">
          🚀 AI routing improved success rate by{" "}
          <span className="font-bold">
            {data.improvementPercentage}%
          </span>
        </p>
      </div>
    </div>
  );
}