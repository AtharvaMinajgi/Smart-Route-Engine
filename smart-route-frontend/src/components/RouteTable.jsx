// import { useGlobal } from "../context/GlobalContext";

// export default function RouteTable() {
//   const { routes } = useGlobal();

//   return (
//     <div className="bg-gray-900 p-6 rounded-xl">
//       <h2 className="mb-4 text-lg font-semibold">Active Routes</h2>

//       <table className="w-full text-sm text-gray-300">
//         <thead>
//           <tr>
//             <th>Route</th>
//             <th>Network</th>
//             <th>Success</th>
//             <th>Latency</th>
//             <th>Risk</th>
//           </tr>
//         </thead>
//         <tbody>
//           {routes.map((r) => (
//             <tr key={r.id} className="border-t border-gray-700">
//               <td>{r.name}</td>
//               <td>{r.network}</td>
//               <td className="text-green-400">
//                 {(r.effectiveSuccessRate * 100).toFixed(1)}%
//               </td>
//               <td>{r.latencyMs} ms</td>
//               <td>{r.riskFactor}</td>
//             </tr>
//           ))}
//         </tbody>
//       </table>
//     </div>
//   );
// }

import { useGlobal } from "../context/GlobalContext";

export default function RouteTable() {
  const { routes } = useGlobal();

  return (
    <div className="bg-[#111827] rounded-xl p-6">
      <h2 className="text-lg font-semibold mb-4 text-green-400">
        Active Routes
      </h2>

      <table className="w-full text-sm">
        <thead className="text-gray-400">
          <tr>
            <th className="text-left">Route</th>
            <th>Network</th>
            <th>Success</th>
            <th>Latency</th>
            <th>Risk</th>
          </tr>
        </thead>

        <tbody>
          {routes.map(r => (
            <tr key={r.id} className="border-t border-gray-700">
              <td className="py-2">{r.name}</td>
              <td className="text-center">{r.network}</td>

              <td className="text-center text-green-400">
                {(r.effectiveSuccessRate * 100).toFixed(1)}%
              </td>

              <td className="text-center">
                {r.baseLatencyMs} ms
              </td>

              <td className="text-center">
                {r.riskFactor}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}