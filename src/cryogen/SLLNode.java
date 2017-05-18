package cryogen;

/**
* @author Zander Labuschagne 23585137
* Node [head -> -> -> -> -> tail]
*/
						//Wrapper Classes
public class SLLNode< T extends Comparable< T > >// Generic Class(String, Integer, Double etc)
{
	private T info;
	private SLLNode< T > next;

	/**
	 * Default Constructor
	 */
	public SLLNode() 
	{
		this(null,null);
	}

	/**
	 * Overloaded Constructor
	 * @param data some type of data
	 */
	public SLLNode(T data) 
	{
		this(data,null);
	}

	/**
	 * Overloaded Constructor
	 * @param data some type of data
	 * @param ptr pointer points to the next data node
	 */
	public SLLNode(T data, SLLNode< T > ptr) 
	{
		setInfo(data); 
		setNext(ptr);
	}

	/**
	 * Set the info of the node
	 * @param info the info
	 */
	public void setInfo(T info)
	{
		this.info = info;
	}

	/**
	 * set the next node
	 * @param next the next node to set
	 */
	public void setNext(SLLNode< T > next)
	{
		this.next = next;
	}

	/**
	 * get the info
	 * @return the info of this node
	 */
	public T getInfo()
	{
		return info;
	}

	/**
	 * get the next node
	 * @return the next node
	 */
	public SLLNode< T > getNext()
	{
		return next;
	}
}