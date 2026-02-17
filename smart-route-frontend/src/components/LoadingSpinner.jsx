export default function LoadingSpinner({ label }) {
  return (
    <div className="flex items-center gap-3 text-green-400 animate-pulse">
      <div className="h-4 w-4 rounded-full bg-green-400" />
      <span>{label || "Loading..."}</span>
    </div>
  );
}