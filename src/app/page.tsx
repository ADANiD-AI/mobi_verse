import Link from 'next/link';

export default function HomePage() {
  return (
    <div>
      <h1>Mobiverse Dashboard</h1>
      <p>Welcome to your virtual universe.</p>
      <Link href="/virtual-screens">Go to Virtual Screens</Link>
    </div>
  );
}
