export default function KPICard({ title, value }) {
  return (
    <div className="bg-[#111827] p-5 rounded-xl shadow">
      <p className="text-gray-400 text-sm">{title}</p>
      <h2 className="text-3xl font-bold text-green-400 mt-2">
        {value}
      </h2>
    </div>
  );
}