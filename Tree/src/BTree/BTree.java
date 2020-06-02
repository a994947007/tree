package BTree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BTree<E> {
	private Node<E> root;
	private Comparator<E> comparator;
	
	public BTree(Comparator<E> comparator){
		this.comparator = comparator;
	}
	
	public boolean contains(E element){
		return node(element) != null;
	}
	
	protected Node<E> node(E element){
		Node<E> node = root;
		boolean flag = false;
		while(node != null){
			int i = 0;
			for(;i<node.elements.size();i++){
				flag = false;
				if(compare(element, node.elements.get(i)) < 0){
					node = node.children.get(i);
					flag = true;
					break;
				}else if(compare(element,node.elements.get(i)) == 0){
					return node;
				}
			}
			if(!flag)node = node.children.get(i);
		}
		return node;
	}
	
	public void add(E element){
		if(root == null){
			root = new Node<>(element,null);
			return;
		}
		
	}
	
	public void remove(E element){
		
	}
	
	@SuppressWarnings({ "unchecked" })
	private int compare(E e1,E e2){
		if(comparator != null){
			return comparator.compare(e1, e2);
		}else{
			//强转使得在没有使用comparator比较器时E必须实现这个接口，又可使得在使用comparator比较器时不实现这个接口
			return ((Comparable<E>)e1).compareTo(e2);	
		}
	}
	
	protected static class Node<E>{
		public List<E> elements;
		public List<Node<E>> children; 
		public Node<E> parent;
		public Node(E element,Node<E> parent){
			elements = new ArrayList<E>();
			children = new ArrayList<Node<E>>();
			this.parent = parent;
			this.addElement(element);
		}
		
		public void addElement(E element){
			this.elements.add(element);
		}
		
		public void removeElement(E element){
			this.elements.remove(element);
		}
		
		public void addChild(Node<E> child){
			this.children.add(child);
		}
		
		public void removeChild(Node<E> child){
			this.children.remove(child);
		} 
	}
}
