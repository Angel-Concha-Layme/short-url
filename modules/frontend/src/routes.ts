export const PUBLIC_ROUTES = {
    HOME: "/" as const,
    AUTH: "/auth" as const,
} as const;

export const PROTECTED_ROUTES = {
    DASHBOARD: "/dashboard" as const,
    SETTINGS: "/settings" as const,
} as const;

export type PublicRoute = (typeof PUBLIC_ROUTES)[keyof typeof PUBLIC_ROUTES];
export type ProtectedRoute = (typeof PROTECTED_ROUTES)[keyof typeof PROTECTED_ROUTES];
export type AppRoute = PublicRoute | ProtectedRoute;

export const ROUTES = {
    public: PUBLIC_ROUTES,
    protected: PROTECTED_ROUTES,
} as const;

export const isProtectedRoute = (route: string): route is ProtectedRoute =>
    Object.values(PROTECTED_ROUTES).includes(route as ProtectedRoute);

export const isPublicRoute = (route: string): route is PublicRoute =>
    Object.values(PUBLIC_ROUTES).includes(route as PublicRoute);
