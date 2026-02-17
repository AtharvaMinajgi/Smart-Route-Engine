import { useState, useEffect } from "react";

export default function AdminRouteEditor({ route, onSave }) {
  const [form, setForm] = useState(route);

  useEffect(() => {
    setForm(route); // 🔥 CRITICAL FIX
  }, [route]);

  const update = (key, value) => {
    setForm({ ...form, [key]: value });
  };

  return (
    <div className="bg-[#0f172a] p-6 rounded-xl space-y-4">
      <h2 className="text-xl font-semibold text-green-400">
        Edit Route: {route.name}
      </h2>

      <div className="grid grid-cols-3 gap-4">
        <Field
          label="Base Success Rate"
          value={form.baseSuccessRate}
          onChange={(v) => update("baseSuccessRate", Number(v))}
        />
        <Field
          label="Risk Factor"
          value={form.riskFactor}
          onChange={(v) => update("riskFactor", Number(v))}
        />
        <Field
          label="Base Latency (ms)"
          value={form.baseLatencyMs}
          onChange={(v) => update("baseLatencyMs", Number(v))}
        />
      </div>

      <button
        onClick={() => onSave(form)}
        className="bg-green-500 px-6 py-2 rounded hover:scale-105 transition"
      >
        Save Route
      </button>
    </div>
  );
}

function Field({ label, value, onChange }) {
  return (
    <div>
      <p className="text-sm text-gray-400">{label}</p>
      <input
        type="number"
        step="0.01"
        value={value ?? ""}
        onChange={(e) => onChange(e.target.value)}
        className="w-full bg-gray-800 p-2 rounded"
      />
    </div>
  );
}