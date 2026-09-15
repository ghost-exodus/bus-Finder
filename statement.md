# Problem Statement

Navigating a city's complex bus network can be overwhelming for residents and tourists alike. Traditional static maps lack the interactivity needed to quickly find the shortest path or the route with the fewest transfers between two locations. This project addresses the need for an efficient, terminal-based application that can calculate optimal bus routes dynamically.

## Scope of the Project
This project provides a Command-Line Interface (CLI) tool specifically designed for the Bhopal public bus network. It reads a static dataset of bus routes and stops, builds a graph representation of the network, and allows users to query for the shortest paths based on either distance or the minimum number of stops. It operates entirely in the terminal without external databases or a Graphical User Interface (GUI).

## Target Users
- **Commuters**: Individuals looking for the quickest way to travel between two points using public transport.
- **Tourists**: Visitors who are unfamiliar with the city's bus routes and need a straightforward tool to plan their journeys.
- **City Planners**: Analysts who want to visualize or calculate network efficiency based on existing routes.

## High-Level Features
- **Shortest Path Calculation (Distance)**: Utilizes Dijkstra's Algorithm to find the route covering the least physical distance.
- **Minimum Stops Calculation (Transfers)**: Utilizes Breadth-First Search (BFS) to find the route with the fewest stops/transfers.
- **Live Tracker Simulation**: A background concurrent thread that simulates live bus locations while the user interacts with the system.
- **Dynamic Terminal UI**: A visually enhanced command-line interface featuring ANSI colors and ASCII-art formatting for a premium feel.
- **Persistent Data Loading**: Reads route and stop data from internal CSV files (File I/O).
