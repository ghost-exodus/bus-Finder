# Bhopal Bus Route Finder



https://github.com/user-attachments/assets/05acc6b5-dcf2-4a7e-b0ac-84b75e6151e3





A dynamic, visually appealing command-line application to navigate Bhopal's public bus network. Built purely in Java, this tool calculates the shortest physical distance between stops using Dijkstra's Algorithm, or the minimum number of transfers using Breadth-First Search (BFS). 

## Features

- **Shortest Distance Routing**: Find the most direct physical route between any two stops using Dijkstra's algorithm.
- **Minimum Transfers Routing**: Find the route with the fewest stop changes using BFS.
- **Visually Stunning CLI**: A highly customized terminal interface featuring ANSI colors, structured borders, and clear table outputs—no GUI required.
- **Live Background Tracker**: Simulates live bus status updates using concurrent background threads.
- **Offline Data Storage**: Reads bus routes directly from bundled CSV files, requiring zero external database installations.

## Technologies Used

- **Java 17+**: Core programming language.
- **Data Structures**: Graph theory (Adjacency List), HashMaps, Queues, and PriorityQueues.
- **Algorithms**: Dijkstra's Algorithm, Breadth-First Search (BFS).
- **Concurrency**: Java standard `Thread` library.
- **File I/O**: Native Java CSV parsing.

## Installation & Setup

This project uses standard Java and does not require Maven, Gradle, or any external dependencies!

1. Clone or download this repository.
2. Open your terminal and navigate to the project directory:
   ```bash
   cd Bus-Route-Finder-Bhopal-Bus-Route-Finder/bus-route-finder
   ```
3. Run the provided batch script to compile and start the application automatically:
   ```bash
   run.bat
   ```
   *Note: If you are on Linux/Mac, you can manually run:*
   ```bash
   mkdir -p out
   find src -name "*.java" > sources.txt
   javac -d out @sources.txt
   java -cp out com.busroute.Main
   ```


## System Flowchart
<img width="2816" height="1536" alt="flwchart392" src="https://github.com/user-attachments/assets/0a774235-6ae2-431c-983e-5e021c04e7b2" />


<br><br><br>

## Testing

You can test the application by starting it and entering stop names exactly as prompted. For example, test paths from `Bairagarh` to `Habibganj`. The application includes built-in exception handling to gracefully recover from invalid inputs or unknown stop names.
