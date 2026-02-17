// import { createContext, useContext, useState } from "react";

// const GlobalContext = createContext(null);

// export default function GlobalProvider({ children }) {
//   const [routes, setRoutes] = useState([]);
//   const [mlStatus, setMlStatus] = useState("Unknown");
//   const [comparisonData, setComparisonData] = useState(null);

//   // 🔑 MUST be empty array (not undefined)
//   const [recentTransactions, setRecentTransactions] = useState([]);

//   return (
//     <GlobalContext.Provider
//       value={{
//         routes,
//         setRoutes,
//         mlStatus,
//         setMlStatus,
//         comparisonData,
//         setComparisonData,
//         recentTransactions,
//         setRecentTransactions,
//       }}
//     >
//       {children}
//     </GlobalContext.Provider>
//   );
// }

// export function useGlobal() {
//   const ctx = useContext(GlobalContext);
//   if (!ctx) throw new Error("useGlobal must be used inside GlobalProvider");
//   return ctx;
// }

import { createContext, useContext, useEffect, useState } from "react";
import {
  getRoutes,
  fetchRecentTransactions,
  checkMLStatus,
} from "../services/api";

const GlobalContext = createContext(null);

export function GlobalProvider({ children }) {
  const [routes, setRoutes] = useState([]);
  const [recentTx, setRecentTx] = useState([]);
  const [mlStatus, setMlStatus] = useState("Unknown");

  const refreshRoutes = async () => {
    const data = await getRoutes();
    setRoutes(data);
  };

  const refreshRecentTx = async () => {
    const data = await fetchRecentTransactions();
    setRecentTx(data);
  };

  const refreshMLStatus = async () => {
    const res = await checkMLStatus();
    setMlStatus(res.ml_ready ? "Trained" : "Not Ready");
  };

  useEffect(() => {
    refreshRoutes();
    refreshRecentTx();
    refreshMLStatus();
  }, []);

  return (
    <GlobalContext.Provider
      value={{
        routes,
        recentTx,
        mlStatus,
        refreshRoutes,
        refreshRecentTx,
        refreshMLStatus,
      }}
    >
      {children}
    </GlobalContext.Provider>
  );
}

export const useGlobal = () => {
  const ctx = useContext(GlobalContext);
  if (!ctx) throw new Error("useGlobal must be used inside GlobalProvider");
  return ctx;
};