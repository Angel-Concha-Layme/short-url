import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";
import { ROUTES, isProtectedRoute } from "./routes";

const AUTH_ROUTE = ROUTES.public.AUTH;
const DASHBOARD_ROUTE = ROUTES.protected.DASHBOARD;

export function middleware(request: NextRequest) {
    const { pathname } = request.nextUrl;
    const token = request.cookies.get("jwt-token");
    const isAuthenticated = !!token;

    if (isProtectedRoute(pathname)) {
        if (!isAuthenticated) {
            const loginUrl = new URL(AUTH_ROUTE, request.url);
            loginUrl.searchParams.set("redirect", pathname);
            return NextResponse.redirect(loginUrl);
        }

        return NextResponse.next();
    }

    if (isAuthenticated && pathname.startsWith(AUTH_ROUTE)) {
        return NextResponse.redirect(new URL(DASHBOARD_ROUTE, request.url));
    }

    return NextResponse.next();
}

export const config = {
    matcher: [
        "/((?!_next/static|_next/image|favicon.ico).*)",
    ],
};
