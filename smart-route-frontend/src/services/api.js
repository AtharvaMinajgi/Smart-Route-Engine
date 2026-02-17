import axios from "axios";

const API_BASE = "http://localhost:8080";

/* ===============================
   TRANSACTIONS
================================ */

// Single transaction
export const runTransaction = async (amount, mode) => {
  const res = await axios.post(
    `${API_BASE}/transactions/single`,
    null,
    {
      params: { amount, mode },
    }
  );
  return res.data;
};

// Recent transactions (TOP 10)
export const fetchRecentTransactions = async () => {
  const res = await axios.get(`${API_BASE}/transactions/recent`);
  return res.data;
};

/* ===============================
   ANALYTICS
================================ */

export const runComparison = async (count) => {
  const res = await axios.get(
    `${API_BASE}/analytics/compare`,
    { params: { count } }
  );
  return res.data;
};

/* ===============================
   ROUTES / ADMIN
================================ */

export const getRoutes = async () => {
  const res = await axios.get(`${API_BASE}/admin/routes`);
  return res.data;
};

export const updateRoute = async (id, payload) => {
  const res = await axios.put(
    `${API_BASE}/admin/routes/${id}`,
    payload
  );
  return res.data;
};

export const resetBandit = async () => {
  await axios.post(`${API_BASE}/admin/reset-bandit`);
};

export const trainML = async () => {
  const res = await axios.post(`${API_BASE}/admin/train-ml`);
  return res.data;
};

export const checkMLStatus = async () => {
  const res = await axios.get(`${API_BASE}/admin/ml-status`);
  return res.data;
};