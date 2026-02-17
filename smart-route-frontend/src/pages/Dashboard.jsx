import { useGlobal } from "../context/GlobalContext";
import KPISection from "../components/KPISection";
import TransactionForm from "../components/TransactionForm";
import RouteTable from "../components/RouteTable";
import RecentTransactions from "../components/RecentTransactions";
import LoadingSpinner from "../components/LoadingSpinner";

export default function Dashboard() {
  const { loading } = useGlobal();

  if (loading) return <LoadingSpinner />;

  return (
    <div className="p-8 space-y-8">
      <h1 className="text-3xl font-bold text-green-400">
        Smart Route Dashboard
      </h1>

      <KPISection />
      <TransactionForm />
      <RouteTable />
      <RecentTransactions />
    </div>
  );
}