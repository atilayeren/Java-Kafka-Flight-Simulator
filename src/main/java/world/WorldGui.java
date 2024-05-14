package world;

import world.entity.CameraLosStatus;
import world.entity.TargetPointPosition;
import world.entity.TowerPosition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

public class WorldGui extends JFrame {

    public static TargetPointPosition target = new TargetPointPosition(170, 300);
    public static final TowerPosition tower = new TowerPosition(500, 475, 0, 425);
    public static CameraLosStatus cameraViewpoint = new CameraLosStatus(0);
    public static CameraLosStatus currCameraViewpoint = new CameraLosStatus(0);

    private final static int WIDTH = 800;
    private final static int HEIGHT = 600;
    private final static int GROUND_HEIGHT = 50;
    private final static int OBJECT_SIZE = 50;

    private final Timer timer;
    private final ImageIcon radar;
    private final ImageIcon plane;
    private ImageIcon camera;

    public static boolean simulationActive = false; // Flag to indicate if simulation is active

    public WorldGui() {
        setTitle("World Simulation");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); //to make the window non-resizable

        // Load images
        radar = new ImageIcon("img/satellite-dish.png");
        camera = mirrorY(new ImageIcon("img/camera.png"));
        plane = new ImageIcon("img/airplane.png");

        // Create ground panel
        JPanel groundPanel = new WorldGuiPanel(null);
        groundPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        add(groundPanel, BorderLayout.CENTER);

        // Create control panel for buttons
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton playButton = new JButton("Play");
        JButton stopButton = new JButton("Stop");
        controlPanel.add(playButton);
        controlPanel.add(stopButton);
        add(controlPanel, BorderLayout.SOUTH);

        // Add action listeners to buttons
        playButton.addActionListener((ActionEvent e)->startSimulation());
        stopButton.addActionListener((ActionEvent e)->stopSimulation());

        // Create timer to refresh the GUI at 1 Hz
        timer = new Timer(100, (ActionEvent e)->onTimer());
        timer.start(); // Start the timer
    }


    private class WorldGuiPanel extends JPanel{
        public WorldGuiPanel(LayoutManager layout){
            super(layout);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw ground
            g.setColor(Color.BLACK);
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            g.drawLine(0, panelHeight - 1, panelWidth, panelHeight - 1);

            // Draw stationary objects
            if (radar != null) {
                g.drawImage(radar.getImage(), tower.getRadarCoordinateX(), tower.getRadarCoordinateY(), OBJECT_SIZE, OBJECT_SIZE, null);
            } else {
                System.err.println("Error: Radar image not found.");
            }

            if (camera != null) {
                g.drawImage(camera.getImage(), tower.getCameraCoordinateX(), tower.getRadarCoordinateY(), OBJECT_SIZE, OBJECT_SIZE, null);
            } else {
                System.err.println("Error: Camera image not found.");
            }

            // Draw movable object (plane)
            if (plane != null) {
                g.drawImage(plane.getImage(), target.getCoordinateX(), target.getCoordinateY(), OBJECT_SIZE, OBJECT_SIZE, null);
                g.setColor(Color.BLACK);
            } else {
                System.err.println("Error: Plane image not found.");
            }
        }
    }

    private void onTimer(){
        if (simulationActive){
            if(cameraViewpoint != null && cameraViewpoint.getViewpoint() != 0){
                cameraViewpoint.setViewpoint( (cameraViewpoint.getViewpoint() + 360)%180 ); // to prevent image mirroring due to positive/negative area change

                camera = rotate(camera, (cameraViewpoint.getViewpoint() - currCameraViewpoint.getViewpoint()));
                currCameraViewpoint.setViewpoint(cameraViewpoint.getViewpoint());
            }
            moveTarget(); // Move the plane randomly
            repaint(); // Redraw the panel
        }
    }

    // Method to move the movable object (plane) randomly
    private void moveTarget() {
        Random random = new Random();
        int direction = random.nextInt(10); // Generate random direction (0-7: mostly right or left, 8: up, 9: down)

        switch (direction) {
            case 0: case 1: case 2: case 3: case 4: case 5:
                target.setCoordinateX(target.getCoordinateX() + 5);// Mostly right
                break;
            case 6: case 7: // Sometimes left
                target.setCoordinateX(target.getCoordinateX() - 5);// Mostly right
                break;
            case 8: // Occasionally Up
                target.setCoordinateY(target.getCoordinateY() - 5);// Mostly right
                break;
            case 9: // Occasionally Down
                target.setCoordinateY(target.getCoordinateY() + 5);// Mostly right
                break;
        }

        // Ensure the movable object (plane) stays within the bounds of the ground panel
        target.setCoordinateX(Math.max(0, Math.min(WIDTH - OBJECT_SIZE, target.getCoordinateX())));
        target.setCoordinateY(Math.max(0, Math.min(HEIGHT - GROUND_HEIGHT - OBJECT_SIZE, target.getCoordinateY())));
    }

    // Method to start the simulation
    private void startSimulation() {
        simulationActive = true; // Set simulation flag to true
        timer.start();
    }

    // Method to stop the simulation
    private void stopSimulation() {
        simulationActive = false; // Set simulation flag to true
        timer.stop();
    }

    public static ImageIcon rotate(ImageIcon icon, double angle) {
        Image image = icon.getImage();

        // Create a BufferedImage to hold the rotated image
        BufferedImage rotatedImage = new BufferedImage(
                image.getWidth(null),
                image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);

        // Create Graphics2D object to draw the rotated image
        Graphics2D g2d = rotatedImage.createGraphics();

        // Rotate the image around its center
        double radians = Math.toRadians(angle);
        int centerX = image.getWidth(null) / 2;
        int centerY = image.getHeight(null) / 2;
        g2d.rotate(radians, centerX, centerY);

        // Draw the rotated image onto the BufferedImage
        g2d.drawImage(image, 0, 0, null);

        // Dispose of the Graphics2D object
        g2d.dispose();

        // Return the rotated image as an ImageIcon
        return new ImageIcon(rotatedImage);
    }

    public static ImageIcon mirrorY(ImageIcon icon) {
    Image image = icon.getImage();

    // Create a BufferedImage to hold the mirrored image
    BufferedImage mirroredImage = new BufferedImage(
            image.getWidth(null),
            image.getHeight(null),
            BufferedImage.TYPE_INT_ARGB);

    // Create Graphics2D object to draw the mirrored image
    Graphics2D g2d = mirroredImage.createGraphics();

    // Mirror the image along the y-axis by scaling with a negative scale factor
    g2d.scale(-1, 1);

    // Draw the mirrored image onto the BufferedImage
    g2d.drawImage(image, -image.getWidth(null), 0, null);

    // Dispose of the Graphics2D object
    g2d.dispose();

    // Return the mirrored image as an ImageIcon
    return new ImageIcon(mirroredImage);
}
}
