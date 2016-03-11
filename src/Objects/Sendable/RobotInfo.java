package Objects.Sendable;

import java.awt.Point;
import java.util.Vector;

import Objects.Direction;
import Objects.WarehouseMap;
import routePlanning.dataStructures.TimePosReservations;

public class RobotInfo implements SendableObject {

	private String name;
	private Point position;
	private Direction direction;
	
	///////////////////////////////////////////////////////////////////////////////////Route Planning///////////////////////////////////////////////////////////////////////////////////
	private Point currentNode;
	private TimePosReservations timePosReservations;
	
	/**
	 * After robot stops at goal and remains stationary(no jobs) it becomes an obstacle and has the node it resides at reserved until the next move
	 */
	private boolean stopped;
	
	///////////////////////////////////////////////////////////////////////////////////Route Planning-DEBUg///////////////////////////////////////////////////////////////////////////////////
	private int robotID;
	private WarehouseMap map;
	private Vector<Direction> pathSequence;
	private int pathSequenceProgress = 0;//Index of the next move
	
	public RobotInfo(int robotID, WarehouseMap map){
		this.robotID = robotID;
		this.map = map;
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//By default, all robots start facing 'north' (which is west IRL)
	public RobotInfo(String name, Point position) {
		this.name = name;
		this.position = position;
		direction = Direction.NORTH;
	}

	public RobotInfo(String name, Point position, Direction direction) {
		this.name = name;
		this.position = position;
		this.direction = direction;
	}
	
	public String getName() {
		return name;
	}
	
	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position)
	{
		this.position = position;
	}

	public Direction getDirection()
	{
		return direction;
	}

	public void setDirection(Direction direction)
	{
		this.direction = direction;
	}

	// toString method for debugging purposes
	@Override
	public String toString() {
		return "RobotInfo [name=" + name + ", position=" + position + ", direction=" + direction + "]";
	}

	/**
	 * Gets the parameters in csv format
	 * @return all parameters, seperated by commas
	 */
	public String parameters() {
		return ("RobotInfo," + name + "," + (int)position.getX() + "," + (int)position.getY() + "," + direction);
	}
	
	///////////////////////////////////////////////////////////////////////////////////Route Planning///////////////////////////////////////////////////////////////////////////////////
	/**
	 * SetUp a stationary robot(no jobs allocated)
	 * @param currentNode
	 */
	public void SetUpStationary(Point currentNode){
		this.currentNode = currentNode;
		stopped = true;
	}
	
	public void SetUpPath(Point currentNode, TimePosReservations timePosReservations){
		this.currentNode = currentNode;
		this.timePosReservations = timePosReservations;
		stopped = false;
	}

	///////////////////////////////////////////////////////////////////Time & Pos Reservations///////////////////////////////////////////////////////////////////////////
	public boolean IsReserved(Point node, int time){
		if (stopped)
			return node.equals(currentNode);// stopped e.g. No reservations yet, therefore return true if the current node is being checked for reservations as this robot is occupying it
		else if (timePosReservations == null)
			return false;//object created yet SetUp Method hasnt been called on this instance
		return timePosReservations.isReserved(node, time);
	}
	
	///////////////////////////////////////////////////////////////////////////////////Route Planning-DEBUg///////////////////////////////////////////////////////////////////////////////////
	public void SetUpPath(Point currentNode, Vector<Direction> pathSequence, TimePosReservations timePosReservations){
		this.currentNode = currentNode;
		this.pathSequence = pathSequence;
		this.timePosReservations = timePosReservations;
		stopped = false;
	}
	
	public Point goToNextNode(){
		if (pathSequenceProgress == pathSequence.size()){
			stopped = true;
			return null;//End of path
		}
		
		switch (pathSequence.get(pathSequenceProgress++)) {
		case NORTH:
			return moveUp();

		case SOUTH:
			return moveDown();	
					
		case EAST:
			return moveRight();
			
		case WEST:
			return moveLeft();
			
		default:
			return null;//Should not happen
		}
	}
	
	private Point move(Point destinationNode){
		//currentNode.status = Node.GOALPATH;
		return currentNode = destinationNode;
		//currentNode.status = " " + robotID + " ";
	}
	
	public Point moveUp() {
		return move(map.getAboveNode(currentNode));
	}
	
	public Point moveDown() {
		return move(map.getBelowNode(currentNode));
	}
	
	public Point moveLeft() {
		return move(map.getLeftNode(currentNode));
	}
	
	public Point moveRight() {
		return move(map.getRightNode(currentNode));
	}
	
	public int getID(){
		return robotID;
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}