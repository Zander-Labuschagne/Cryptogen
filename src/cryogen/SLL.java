package cryogen;

/**
* @author Zander Labuschagne 23585137
* Generic Singly Linked List [head -> -> -> -> -> tail]
*/

public class SLL< T extends Comparable< T > >
{
		private SLLNode< T > head;
		private SLLNode< T > tail;

	/**
	 * Default COnstructor
	 */
	public SLL()
		{
			head = tail = null;
		}

	/**
	 * check if list is empty
	 * @return true if list is empty false otherwise
	 */
	public boolean isEmpty()
		{
			return head == null;
		}

	/**
	 * add new data node to head
	 * @param data node to add
	 */
	public void addToHead(T data)
	{
		head = new SLLNode< T >(data, head);
		if (tail == null)
			tail = head;
	}

	/**
	 * add new data node to tail
	 * @param data node to add
	 */
	public void addToTail(T data)
	{
		if (!isEmpty())
		{
			tail.setNext(new SLLNode(data));
			tail = tail.getNext();
		}
		else
			head = tail = new SLLNode< T >(data);
	}

	/**
	 * delete data node from head
	 * @return the data node deleted
	 */
	public T deleteFromHead()
	{ // delete the head and return its info;
		if (isEmpty())
			return null;
		T data = head.getInfo();
		if (head == tail) // if only one node on the list;
			head = tail = null;
		else
			head = head.getNext();
		return data;
	}

	/**
	 * delete data node from tail
	 * @return the data node deleted
	 */
	public T deleteFromTail()
	{ // delete the tail and return its info;
		if (isEmpty())
			return null;
		T data = tail.getInfo();
		if (head == tail) // if only one node in the list;
			head = tail = null;
		else
		{// if more than one node in the list,
			SLLNode< T > tmp;    // find the predecessor of tail;
			for(tmp = head; tmp.getNext() != tail; tmp = tmp.getNext());
			tail = tmp;        // the predecessor of tail becomes tail;
			tail.setNext(null);
		}
		return data;
	}

	/**
	 * original delete data
	 * @param data data to delete
	 */
	public void delete(T data)
	{  // delete the node with an element data;
		if (!isEmpty())
			if (head == tail && data.equals(head.getInfo())) // if only one
				head = tail = null;       // node on the list;
			else if (data.equals(head.getInfo())) // if more than one node on the list;
				head = head.getNext();    // and el is in the head node;
			else
			{         // if more than one node in the list
				SLLNode< T > pred;
				SLLNode< T > tmp;// and data is in a nonhead node;
				for (pred = head, tmp = head.getNext();	tmp != null && !tmp.getInfo().equals(data);	  pred = pred.getNext(), tmp = tmp.getNext());
				if (tmp != null)
				{   // if el was found;
					pred.setNext(tmp.getNext());
					if (tmp == tail) // if el is in the last node;
						tail = pred;
				}
			}
	}

	/**
	 * Custom delete node n
	 * @param n number of node to delete
	 * @return the node deleted
	 */
	public T deleteNode(int n)
	{
		int i;
		SLLNode< T > temp;
		T info = null;

		if(!(isEmpty()))
			if(n == 1)
				if(head == tail)
				{
					info = head.getInfo();
					head = tail = null;
				}
				else
				{
					info = head.getInfo();
					head = head.getNext();
				}
			else
			{
				for(i = 1, temp = head; i < n - 1 && temp.getNext() != null; i++, temp = temp.getNext());

				if(temp.getNext() != null)
				{
					info = temp.getNext().getInfo();
					temp.setNext(temp.getNext().getNext());
				}
			}
		return info;
	}

	/**
	 * insert a node ?
	 * @param data to insert
	 */
	public void insert(T data)
	{
		SLLNode<T> voor = head;

		if (isEmpty())
			addToHead(data);
		else if (data.compareTo(tail.getInfo()) > 0)
			addToTail(data);
		else if (data.compareTo(head.getInfo()) <= 0)
			addToHead(data);
		else
		{
			//data > getInfo ? 1 : -1              = 0
			for (voor = head; voor.getNext() != null && data.compareTo(voor.getNext().getInfo()) > 0; voor = voor.getNext())
				;
			voor.setNext(new SLLNode<T>(data, voor.getNext()));
		}
	}

	/**
	 * print all data
	 * @return string to print
	 */
	public String printAll()
	{
		String temp = "";

		for (SLLNode<T> tmp = head; tmp != null; tmp = tmp.getNext())
			temp += (tmp.getInfo() + " ");
				
		return temp;
	}

	/**
	 * check if data is in list
	 * @param data to check
	 * @return true if data is found
	 */
	public boolean isInList(T data)
	{
		SLLNode<T> tmp;
		for (tmp = head; tmp != null && !tmp.getInfo().equals(data); tmp = tmp.getNext());
		return tmp != null;
	}
}
