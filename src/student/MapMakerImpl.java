package student;

import model.*;

import java.io.*;
import java.util.LinkedList;

/**
 * Loads and saves maps.
 *
 * @author Matt Agger, mea5692@rit.edu
 * @author Zachary Talis, zft5955@rit.edu
 */
public class MapMakerImpl implements MapMaker {

    // The LinkedList of all stations on the map
    private LinkedList<Station> stations = new LinkedList<>();

    /**
     * Load a map using the data in the given input stream.
     * @param in The {@link InputStream} used to read the {@link RailroadMap
     * map} data.
     * @return the map read from the given InputStream.
     * @throws RailroadBaronsException when the provided map file
     * cannot be read properly.
     */
    @Override
    public RailroadMap readMap(InputStream in) throws RailroadBaronsException {

        // Read from the InputStream
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        // The double array of all spaces on the map
        Space[][] spaces;
        // The LinkedList of all stations on the map
        stations = new LinkedList<>();
        // The LinkedList of all routes on the map
        LinkedList<Route> routes = new LinkedList<>();

        try {

            // Whether or not reader is reading route lines
            boolean nowRoutes = false;
            // The latest line from reader
            String line = reader.readLine();

            // Loop until there are no more lines left in the file
            while (line != null) {

                // Break apart the line
                String[] lineSplit = line.split(" ", 4);

                if (lineSplit[0].equals("##ROUTES##")) nowRoutes = true;
                else if (!nowRoutes) {
                    // Add new station
                    stations.add(new StationImpl(Integer.parseInt(lineSplit[1]),
                            Integer.parseInt(lineSplit[2]),
                            lineSplit[0] + " " + lineSplit[3]));
                } else {
                    // Add new route

                    // Find origin, destination, and owner of route
                    Station origin =
                            stations.get(Integer.parseInt(lineSplit[0]));
                    Station destination =
                            stations.get(Integer.parseInt(lineSplit[1]));
                    Baron owner;
                    switch (lineSplit[2]) {
                        case "UNCLAIMED": owner = Baron.UNCLAIMED;
                            break;
                        case "BLUE": owner = Baron.BLUE;
                            break;
                        case "GREEN": owner = Baron.GREEN;
                            break;
                        case "RED": owner = Baron.RED;
                            break;
                        default: owner = Baron.YELLOW;
                    }

                    // Find orientation of route
                    Orientation orientation;
                    if (origin.getRow() == destination.getRow())
                        orientation = Orientation.HORIZONTAL;
                    else orientation = Orientation.VERTICAL;

                    // Initiate tracksInRoute and newRoute
                    LinkedList<Track> tracksInRoute = new LinkedList<>();
                    RouteImpl newRoute = new RouteImpl(owner,
                            origin, destination, orientation, tracksInRoute);

                    // Set coordinates
                    LinkedList<Integer> coords = new LinkedList<>();
                    if (orientation == Orientation.HORIZONTAL) {
                        int i = Math.min(origin.getCol(), destination.getCol()) + 1;
                        while (i < Math.max(origin.getCol(),
                                destination.getCol())) {
                            coords.add(i);
                            i++;
                        }
                    } else {
                        int i = Math.min(origin.getRow(), destination.getRow()) + 1;
                        while (i < Math.max(origin.getRow(),
                                destination.getRow())) {
                            coords.add(i);
                            i++;
                        }
                    }

                    // Add the corresponding tracks to tracksInRoute
                    for (int coord : coords) {
                        if (orientation == Orientation.HORIZONTAL) {
                            tracksInRoute.add(new TrackImpl(origin.getRow(),
                                    coord, newRoute));
                        } else {
                            tracksInRoute.add(new TrackImpl(coord,
                                    origin.getCol(), newRoute));
                        }
                    }

                    // Add newRoute to routes
                    routes.add(newRoute);
                }

                // Move onto the next line
                line = reader.readLine();
                // Go back and see if there is a new line to be read
            }


            // Generate spaces                             //
            // (Use the station coordinates in a for loop) //

            // Find the dimensions of the map
            int bottomRow = -1;
            int bottomCol = -1;
            for (Station station : stations) {
                if (station.getRow() > bottomRow || bottomRow == -1)
                    bottomRow = station.getRow();
                if (station.getCol() > bottomCol || bottomCol == -1)
                    bottomCol = station.getCol();
            }

            // Add empty spaces (all empty for now)
            spaces = new Space[bottomRow+1][bottomCol+1];
            for (int row = 0; row <= bottomRow; row++) {
                for (int col = 0; col <= bottomCol; col++) {
                    spaces[row][col] = new SpaceImpl(row, col);
                }
            }


            // Add stations and routes to spaces //
            // (replacing some existing spaces)  //
            for (Station station : stations) {
                spaces[station.getRow()][station.getCol()] = station;
            }
            for (Route route : routes) {
                for (Track track : route.getTracks()) {
                    spaces[track.getRow()][track.getCol()] = track;
                }
            }


        } catch (IOException exc) {
            throw new RailroadBaronsException
                    ("Provided map file cannot be read properly.");
        }

        // The new map contains the generated spaces and routes
        return new RailroadMapImpl(spaces, routes);
    }

    /**
     * Write the specified map in the Railroad Barons map file format to the
     * given output stream.
     * @param map The {@link RailroadMap map} to write out to the
     * {@link OutputStream}.
     * @param out The {@link OutputStream} to which the
     * {@link RailroadMap map} data should be written.
     *
     * @throws RailroadBaronsException when the provided output path
     * cannot be written to properly.
     */
    @Override
    public void writeMap(RailroadMap map, OutputStream out)
            throws RailroadBaronsException {

        // The LinkedList of lines corresponding to different stations
        LinkedList<String> stationLines = new LinkedList<>();
        // The LinkedList of lines corresponding to different routes
        LinkedList<String> routeLines = new LinkedList<>();

        try {

            // Write to the OutputStream
            BufferedWriter writer =
                    new BufferedWriter(new OutputStreamWriter(out));

            // Generate lines to write for the different stations
            for (Station station : stations) {
                stationLines.add(String.valueOf(stations.indexOf(station)) +
                String.valueOf(station.getRow()) +
                String.valueOf(station.getCol()) +
                station.getName());
            }

            // Generate lines to write for the different routes
            for (Route route : map.getRoutes()) {

                // Add the station indices to the new line
                int[] knownStations = new int[2];
                knownStations[0] = stations.indexOf(route.getOrigin());
                knownStations[1] = stations.indexOf(route.getDestination());

                // Add route ownership to the new line
                String owner;
                switch (route.getBaron()) {
                    case UNCLAIMED: owner = "UNCLAIMED";
                        break;
                    case BLUE: owner = "BLUE";
                        break;
                    case GREEN: owner = "GREEN";
                        break;
                    case RED: owner = "RED";
                        break;
                    default: owner = "YELLOW";
                }

                // Add the new line to routeLines
                routeLines.add(String.valueOf(knownStations[0]) + " " +
                        String.valueOf(knownStations[1]) + " " + owner);
            }

            // Write to out
            for (String stationLine : stationLines) {
                writer.write(stationLine); }
            writer.write("##ROUTES##");
            for (String routeLine : routeLines) {
                writer.write(routeLine); }

        } catch (IOException exc) {
            throw new RailroadBaronsException
                    ("Provided output path cannot be written to properly.");
        }
    }
}
