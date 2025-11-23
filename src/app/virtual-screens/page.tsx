"use client";
import React, { useEffect, useState } from "react";
import { collection, query, where, onSnapshot, addDoc } from "firebase/firestore";
import { db, auth } from "@/lib/firebase";
import "./virtual-screens.css";
import LoadingSpinner from "@/components/LoadingSpinner";
import AIPaint from "./apps/AIPaint";
import Vault from "./apps/Vault"; // 1. Import Vault component

type AppItem = {
  id: string;
  name: string;
  icon: string;
  url: string;
  category: string;
  isActive?: boolean;
};

type ScreenData = {
  screenNumber: number;
  screenName: string;
  userId: string;
  screenType: string;
  theme: string;
  wallpaper: string;
  installedApps: AppItem[];
  isActive: boolean;
  createdAt?: any;
  updatedAt?: any;
  id?: string;
};

// 2. Register Vault component
const internalApps: Record<string, React.ComponentType> = {
  "/ai/image-generator": AIPaint,
  "/secure/vault": Vault,
};

// 3. Add Vault to default apps on screen 5
const defaultApps: Record<number, Omit<ScreenData, "userId" | "createdAt" | "updatedAt">> = {
  1: {
    screenNumber: 1,
    screenName: "Work Screen",
    screenType: "android",
    theme: "professional",
    wallpaper: "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=400",
    installedApps: [
      { id: "gmail", name: "Gmail", icon: "https://cdn-icons-png.flaticon.com/512/732/732200.png", url: "https://mail.google.com", category: "productivity" },
      { id: "docs", name: "Docs", icon: "https://cdn-icons-png.flaticon.com/512/732/732220.png", url: "https://docs.google.com", category: "productivity" }
    ],
    isActive: true
  },
  2: {
    screenNumber: 2,
    screenName: "Islamic Screen",
    screenType: "android",
    theme: "islamic",
    wallpaper: "https://images.unsplash.com/photo-1542816417-0983c9c9ad53?w=400",
    installedApps: [
      { id: "prayer-times", name: "Prayer Times", icon: "https://cdn-icons-png.flaticon.com/512/3406/3406886.png", url: "/islamic/prayer-times", category: "islamic" },
      { id: "quran", name: "Holy Quran", icon: "https://cdn-icons-png.flaticon.com/512/2806/2806051.png", url: "/islamic/quran", category: "islamic" }
    ],
    isActive: true
  },
  3: { screenNumber: 3, screenName: "Entertainment", screenType: "android", theme: "entertainment", wallpaper: "https://images.unsplash.com/photo-1574375927938-d5a98e8ffe85?w=400", installedApps: [{ id: "youtube", name: "YouTube", icon: "https://cdn-icons-png.flaticon.com/512/1384/1384060.png", url: "https://youtube.com", category: "entertainment" }], isActive: true },
  4: { screenNumber: 4, screenName: "Social Media", screenType: "android", theme: "social", wallpaper: "https://images.unsplash.com/photo-1611262588024-d12430b98920?w=400", installedApps: [{ id: "whatsapp", name: "WhatsApp", icon: "https://cdn-icons-png.flaticon.com/512/174/174879.png", url: "https://web.whatsapp.com", category: "social" }], isActive: true },
  5: {
    screenNumber: 5,
    screenName: "AI & Security",
    screenType: "android",
    theme: "ai",
    wallpaper: "https://images.unsplash.com/photo-1677442136019-21780ecad995?w=400",
    installedApps: [
      { id: "ai-image", name: "AI Image Generator", icon: "https://cdn-icons-png.flaticon.com/512/4341/4341139.png", url: "/ai/image-generator", category: "ai" },
      { id: "secure-vault", name: "Secure Vault", icon: "https://cdn-icons-png.flaticon.com/512/2889/2889676.png", url: "/secure/vault", category: "security" }
    ],
    isActive: true
  },
  6: { screenNumber: 6, screenName: "Development", screenType: "android", theme: "developer", wallpaper: "https://images.unsplash.com/photo-1461749280684-dccba630e2f6?w=400", installedApps: [{ id: "github", name: "GitHub", icon: "https://cdn-icons-png.flaticon.com/512/733/733553.png", url: "https://github.com", category: "development" }], isActive: true }
};

export default function VirtualScreensPage() {
  const [screens, setScreens] = useState<ScreenData[]>([]);
  const [activeScreen, setActiveScreen] = useState<number>(1);
  const [loading, setLoading] = useState<boolean>(true);
  const [currentApp, setCurrentApp] = useState<AppItem | null>(null);

  useEffect(() => {
    const uid = auth.currentUser?.uid;
    if (!uid) {
      setLoading(false);
      return;
    }

    const q = query(collection(db, "virtualScreens"), where("userId", "==", uid));
    const unsubscribe = onSnapshot(q, async (snapshot) => {
      if (snapshot.empty) {
        await createDefaultScreens(uid);
        return;
      }
      const arr: ScreenData[] = [];
      snapshot.forEach((d) => {
        arr.push({ id: d.id, ...(d.data() as any) });
      });
      arr.sort((a, b) => a.screenNumber - b.screenNumber);
      setScreens(arr);
      setLoading(false);
    }, (err) => {
      console.error("Snapshot error:", err);
      setLoading(false);
    });

    return () => unsubscribe();
  }, []);

  const createDefaultScreens = async (uid: string) => {
    try {
      for (let i = 1; i <= 6; i++) {
        const base = defaultApps[i];
        await addDoc(collection(db, "virtualScreens"), {
          ...base,
          userId: uid,
          createdAt: new Date(),
          updatedAt: new Date()
        });
      }
    } catch (err) {
      console.error("Error creating default screens", err);
    }
  };

  const renderAppContent = () => {
    if (!currentApp) return null;

    if (currentApp.url.startsWith("http")) {
      return <iframe src={currentApp.url} title={currentApp.name} className="app-iframe" sandbox="allow-same-origin allow-scripts allow-forms" />;
    }

    const AppComponent = internalApps[currentApp.url];
    if (AppComponent) {
      return <AppComponent />;
    }

    return (
      <div className="internal-app">
        <h2>🚧 {currentApp.name}</h2>
        <p>Feature under development</p>
        <p>URL: {currentApp.url}</p>
      </div>
    );
  };

  if (loading) return <div className="loading-wrapper"><LoadingSpinner /><p>Loading Virtual Screens...</p></div>;

  const currentScreen = screens.find(s => s.screenNumber === activeScreen);

  return (
    <div className="virtual-screens-container">
      <div className="screen-tabs">
        {[1,2,3,4,5,6].map(n => (
          <button key={n} className={`screen-tab ${activeScreen === n ? "active":""}`} onClick={() => { setActiveScreen(n); setCurrentApp(null); }}>
            <div className="screen-number">{n}</div>
            <div className="screen-name">{screens.find(s => s.screenNumber === n)?.screenName || `Screen ${n}`}</div>
          </button>
        ))}
      </div>

      <div className="screen-display">
        { currentApp ? (
          <div className="app-container">
            <div className="app-header">
              <button className="close-app-btn" onClick={() => setCurrentApp(null)}>✕</button>
              <h3>{currentApp.name}</h3>
            </div>
            <div className="app-content">
              {renderAppContent()}
            </div>
          </div>
        ) : (
          <div className={`home-screen ${currentScreen?.theme || ""}`} style={{ backgroundImage: `linear-gradient(rgba(0,0,0,0.3), rgba(0,0,0,0.3)), url(${currentScreen?.wallpaper})` }}>
            <div className="screen-header"><h2>{currentScreen?.screenName}</h2><div className="screen-info">Screen {activeScreen} of 6</div></div>
            <div className="apps-grid">
              {currentScreen?.installedApps?.map(app => (
                <div key={app.id} className="app-icon" onClick={() => setCurrentApp(app)}>
                  <div className="app-icon-container"><img src={app.icon} alt={app.name} /></div>
                  <div className="app-name">{app.name}</div>
                </div>
              ))}
            </div>
            <div className="quick-actions"><button className="quick-action-btn">+ Add App</button><button className="quick-action-btn">🎨 Customize</button><button className="quick-action-btn">⚙️ Settings</button></div>
          </div>
        )}
      </div>

      <div className="status-bar">
        <div className="status-left">📱 Virtual Android <span>🔋100%</span> <span>📶4G</span></div>
        <div className="status-center">{new Date().toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'})}</div>
        <div className="status-right">🔔 📞 💬</div>
      </div>
    </div>
  );
}
