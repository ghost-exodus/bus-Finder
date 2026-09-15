package com.busroute.cli;

import com.busroute.algorithm.DijkstraSolver;
import com.busroute.model.Route;
import com.busroute.model.Stop;
import com.busroute.service.RouteService;
import com.busroute.service.SearchService;
import com.busroute.util.Logger;

import java.util.List;
import java.util.Scanner;

public class MenuHandler {
    private RouteService routeService;
    private SearchService searchService;
    private Scanner scanner;

    // ANSI Colors
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String BLUE = "\u001B[34m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String PURPLE = "\u001B[35m";
    private static final String BOLD = "\u001B[1m";

    public MenuHandler(RouteService routeService, SearchService searchService) {
        this.routeService = routeService;
        this.searchService = searchService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println(CYAN + BOLD + "╔══════════════════════════════════════════╗");
        System.out.println("║      BHOPAL BUS ROUTE FINDER v1.0        ║");
        System.out.println("║   Navigate the city's bus network with   ║");
        System.out.println("║        ease - powered by graphs!         ║");
        System.out.println("╚══════════════════════════════════════════╝" + RESET);
        System.out.println();

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> handleBFS();
                case "2" -> handleDijkstra();
                case "3" -> handleListStopsOnRoute();
                case "4" -> handleRoutesThrough();
                case "5" -> handleSearch();
                case "6" -> {
                    System.out.println(GREEN + "\nThank you for using Bhopal Bus Route Finder. Safe travels!" + RESET);
                    running = false;
                }
                default -> System.out.println(RED + "\n[!] Invalid choice. Please enter a number between 1 and 6.\n" + RESET);
            }
        }
    }

    private void printMenu() {
        System.out.println(YELLOW + "┌------------------------------------------┐");
        System.out.println("│               MAIN MENU                  │");
        System.out.println("├------------------------------------------┤");
        System.out.println("│  1. Find route (fewest stops)     [BFS]  │");
        System.out.println("│  2. Find route (fastest time) [Dijkstra] │");
        System.out.println("│  3. List all stops on a route            │");
        System.out.println("│  4. Find routes through a stop           │");
        System.out.println("│  5. Search stop by name                  │");
        System.out.println("│  6. Exit                                 │");
        System.out.println("└------------------------------------------┘" + RESET);
        System.out.print(CYAN + "Enter your choice: " + RESET);
    }

    private Stop promptAndResolveStop(String label) {
        System.out.print(CYAN + "Enter " + label + " (name, ID, or type 'list' for all stops): " + RESET);
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println(RED + "[!] Input cannot be empty." + RESET);
            return null;
        }
        
        if (input.equalsIgnoreCase("list")) {
            System.out.println(YELLOW + "\n-- All Available Stops --" + RESET);
            List<Stop> allStops = searchService.getAllStops();
            for (Stop s : allStops) {
                System.out.println(GREEN + "  * " + RESET + s.getName() + " (ID: " + s.getStopId() + ")");
            }
            System.out.println();
            return promptAndResolveStop(label); // Prompt again
        }

        Stop stop = searchService.resolveStop(input);
        if (stop != null) return stop;

        List<Stop> matches = searchService.searchByName(input);
        if (matches.isEmpty()) {
            System.out.println(RED + "[!] No stop found matching: " + input + RESET);
            return null;
        }

        System.out.println(YELLOW + "\nMultiple matches found - please select:" + RESET);
        for (int i = 0; i < matches.size(); i++) {
            System.out.println("  " + GREEN + (i + 1) + RESET + ". " + matches.get(i));
        }
        System.out.print(CYAN + "Enter number: " + RESET);

        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx >= 0 && idx < matches.size()) {
                return matches.get(idx);
            }
        } catch (NumberFormatException ignored) {}

        System.out.println(RED + "[!] Invalid selection." + RESET);
        return null;
    }

    private void handleBFS() {
        System.out.println(PURPLE + BOLD + "\n-- Find Route (Fewest Stops — BFS) --\n" + RESET);

        Stop source = promptAndResolveStop("source stop");
        if (source == null) { System.out.println(); return; }

        Stop dest = promptAndResolveStop("destination stop");
        if (dest == null) { System.out.println(); return; }

        List<Stop> path = routeService.findShortestPath(source.getStopId(), dest.getStopId());

        if (path.isEmpty()) {
            System.out.println(RED + "\n[X] No route found from " + source.getName() + " to " + dest.getName() + ".\n" + RESET);
            Logger.log("BFS | " + source.getName() + " -> " + dest.getName() + " | No route found");
            return;
        }

        System.out.println(GREEN + BOLD + "\n[+] Route found! (" + path.size() + " stops)\n" + RESET);
        printPathWithRoutes(path);

        Logger.log("BFS | " + source.getName() + " -> " + dest.getName() + " | " + path.size() + " stops");
        System.out.println();
    }

    private void handleDijkstra() {
        System.out.println(PURPLE + BOLD + "\n-- Find Route (Fastest Time — Dijkstra) --\n" + RESET);

        Stop source = promptAndResolveStop("source stop");
        if (source == null) { System.out.println(); return; }

        Stop dest = promptAndResolveStop("destination stop");
        if (dest == null) { System.out.println(); return; }

        DijkstraSolver.PathResult result = routeService.findFastestPath(source.getStopId(), dest.getStopId());

        if (result.getPath().isEmpty()) {
            System.out.println(RED + "\n[X] No route found from " + source.getName() + " to " + dest.getName() + ".\n" + RESET);
            Logger.log("Dijkstra | " + source.getName() + " -> " + dest.getName() + " | No route found");
            return;
        }

        List<Stop> path = result.getPath();
        System.out.println(GREEN + BOLD + "\n[+] Fastest route found! (Total time: " + result.getTotalTime() + " minutes)\n" + RESET);

        String prevRoute = null;
        for (int i = 0; i < path.size(); i++) {
            Stop stop = path.get(i);
            if (i < path.size() - 1) {
                String route = result.getRouteForEdge(stop.getStopId(), path.get(i + 1).getStopId());
                if (prevRoute != null && !route.equals(prevRoute)) {
                    System.out.println(YELLOW + "     [>] INTERCHANGE at " + stop.getName() + " - switch to " + route + RESET);
                }
                System.out.println(CYAN + "  " + (i + 1) + ". " + stop.getName() + RESET + "  [" + BLUE + route + RESET + "]");
                prevRoute = route;
            } else {
                System.out.println(GREEN + "  " + (i + 1) + ". " + stop.getName() + "  [Destination]" + RESET);
            }
        }

        Logger.log("Dijkstra | " + source.getName() + " -> " + dest.getName()
                + " | " + path.size() + " stops, " + result.getTotalTime() + " min");
        System.out.println();
    }

    private void printPathWithRoutes(List<Stop> path) {
        String prevRoute = null;
        for (int i = 0; i < path.size(); i++) {
            Stop stop = path.get(i);
            if (i < path.size() - 1) {
                String route = routeService.getRouteOnEdge(stop.getStopId(), path.get(i + 1).getStopId());
                if (prevRoute != null && !route.equals(prevRoute)) {
                    System.out.println(YELLOW + "     [>] INTERCHANGE at " + stop.getName() + " - switch to " + route + RESET);
                }
                System.out.println(CYAN + "  " + (i + 1) + ". " + stop.getName() + RESET + "  [" + BLUE + route + RESET + "]");
                prevRoute = route;
            } else {
                System.out.println(GREEN + "  " + (i + 1) + ". " + stop.getName() + "  [Destination]" + RESET);
            }
        }
    }

    private void handleListStopsOnRoute() {
        System.out.println(PURPLE + BOLD + "\n-- List Stops on a Route --\n" + RESET);
        System.out.println(YELLOW + "Available routes:" + RESET);
        for (Route route : routeService.getAllRoutes()) {
            System.out.println(CYAN + "  * " + RESET + route);
        }
        System.out.print(CYAN + "\nEnter route number (e.g., Route1): " + RESET);
        String routeNum = scanner.nextLine().trim();

        Route route = routeService.getRouteByNumber(routeNum);
        if (route == null && routeNum.matches("\\d+")) {
            route = routeService.getRouteByNumber("Route" + routeNum); // Auto-append "Route" if just a number is entered
        }
        if (route == null) {
            System.out.println(RED + "[!] Route not found: " + routeNum + "\n" + RESET);
            return;
        }

        System.out.println(GREEN + "\n" + route.getRouteNumber() + ": " + route.getStartTerminus() + " → " + route.getEndTerminus() + "\n" + RESET);
        List<Stop> stops = route.getStopSequence();
        for (int i = 0; i < stops.size(); i++) {
            System.out.println(CYAN + "  " + (i + 1) + ". " + RESET + stops.get(i).getName() + " (" + stops.get(i).getZone() + ")");
        }
        System.out.println();
    }

    private void handleRoutesThrough() {
        System.out.println(PURPLE + BOLD + "\n-- Find Routes Through a Stop --\n" + RESET);

        Stop stop = promptAndResolveStop("stop");
        if (stop == null) { System.out.println(); return; }

        List<String> routes = routeService.findRoutesThrough(stop.getStopId());
        if (routes.isEmpty()) {
            System.out.println(RED + "[!] No routes serve this stop.\n" + RESET);
            return;
        }

        System.out.println(GREEN + "\nRoutes through " + stop.getName() + ":" + RESET);
        for (String r : routes) {
            System.out.println(CYAN + "  * " + RESET + r);
        }
        System.out.println();
    }

    private void handleSearch() {
        System.out.println(PURPLE + BOLD + "\n-- Search Stop by Name --\n" + RESET);
        System.out.print(CYAN + "Enter search term (min 3 characters): " + RESET);
        String query = scanner.nextLine().trim();

        if (query.length() < 3) {
            System.out.println(RED + "[!] Please enter at least 3 characters.\n" + RESET);
            return;
        }

        List<Stop> results = searchService.searchByName(query);
        if (results.isEmpty()) {
            System.out.println(YELLOW + "No stops found matching: " + query + "\n" + RESET);
            return;
        }

        System.out.println(GREEN + "\nMatching stops:" + RESET);
        for (Stop stop : results) {
            System.out.println(CYAN + "  * " + RESET + stop);
        }
        if (results.size() == 10) {
            System.out.println(YELLOW + "  (showing first 10 results - try a more specific query)" + RESET);
        }
        System.out.println();
    }
}
