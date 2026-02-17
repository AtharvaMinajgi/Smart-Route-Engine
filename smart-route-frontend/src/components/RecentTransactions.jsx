import { useGlobal } from "../context/GlobalContext";

export default function RecentTransactions() {
  const { recentTx } = useGlobal();

  if (!Array.isArray(recentTx) || recentTx.length === 0) {
    return (
      <div className="bg-gray-900 p-4 rounded text-gray-400">
        No recent transactions yet
      </div>
    );
  }

  return (
    <div className="bg-gray-900 p-4 rounded">
      <h3 className="text-green-400 mb-3">Recent Transactions</h3>

      {recentTx.slice(0, 10).map((tx) => (
        <div
          key={tx.id}
          className="grid grid-cols-5 text-sm border-b border-gray-700 py-1"
        >
          <span>{tx.routeName}</span>
          <span>{tx.routingType}</span>
          <span className={tx.result === "SUCCESS" ? "text-green-400" : "text-red-400"}>
            {tx.result}
          </span>
          <span>{tx.latencyMs} ms</span>
          <span>{(tx.scoreUsed * 100).toFixed(1)}%</span>
        </div>
      ))}
    </div>
  );
}