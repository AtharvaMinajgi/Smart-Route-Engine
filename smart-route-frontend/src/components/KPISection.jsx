import { useGlobal } from "../context/GlobalContext";

export default function KPISection() {
  const { routes, mlStatus } = useGlobal();

  return (
    <div className="grid grid-cols-4 gap-4">
      <div className="bg-gray-800 p-4 rounded">
        <p className="text-gray-400">Active Routes</p>
        <p className="text-2xl text-green-400">{routes.length}</p>
      </div>

      <div className="bg-gray-800 p-4 rounded">
        <p className="text-gray-400">ML Status</p>
        <p className="text-2xl text-green-400">{mlStatus}</p>
      </div>
    </div>
  );
}