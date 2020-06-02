package BinarySearchTree;
import java.util.Comparator;

import binaryTree.BinaryTree;


public class BSTreeImpl<E> extends BinaryTree<E> implements BSTree<E>{
	private Comparator<E> comparator;
	//可以传递Object的类型，比较方式需要外部传递
	public BSTreeImpl(Comparator<E> comparator){
		super();
		this.comparator = comparator;
	}
	//默认构造器，只能传递int，float，double，char等类型
	public BSTreeImpl(){
		this(null);
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

	@Override
	public void add(E element) {
		elementIsNullCheck(element);
		super.size ++;
		if(root == null){
			root = createNode(element, null);
			root.parent = null;
			afterAdd(root);
			return;
		}
		Node<E> parent = root;
		Node<E> node = root;
		int cmp = 0;
		while(node != null){
			parent = node;
			cmp = compare(element, node.element);
			if(cmp < 0){
				node = node.left;
			}else if(cmp > 0){
				node = node.right;
			}else{
				node.element = element;	//由于compare只比较了某些属性，但其他属性可能不同，这里使用覆盖
				return;
			}
		}
		Node<E> newNode = createNode(element, parent);
		if(cmp > 0){
			parent.right = newNode;
		}else{
			parent.left = newNode;
		}
		afterAdd(newNode);
	}
	
	protected void afterAdd(Node<E> node){}

	@Override
	public void remove(E element) {
		elementIsNullCheck(element);
		remove(node(element));
	}
	
	private void remove(Node<E> node){
		if(node == null) return;
		size --;
		//删除度为2的结点
		if(node.hasTwoChildren()){
			Node<E> s = successor(node);
			node.element = s.element;
			//remove(s); return;
			node = s;
		}
		//删除度为1或者0的结点
		Node<E> replacement = node.left != null?node.left : node.right;
		if(replacement != null){	//度为1
			replacement.parent = node.parent;
			if(node.isLeftChild()){
				node.parent.left = replacement;
			}else if(node.isRightChild()){
				node.parent.right = replacement;
			}else if(node == root){
				root = replacement;
				root.parent = null;
			}
		}else{	//度为0
			if(node.parent == null){	//根结点
				root = null;
			}else{
				if(node.isLeftChild()){
					node.parent.left = null;
				}else{
					node.parent.right = null;
				}
			}
		}
		afterRemove(node,replacement);
	}

	protected void afterRemove(binaryTree.BinaryTree.Node<E> node,Node<E> replacement) {}
	
	@Override
	public boolean contains(E element) {
		elementIsNullCheck(element);
		return node(element) != null;
	}
	
	//根据传入的element获取对应的node
	private Node<E> node(E element){
		Node<E> node = root;
		while(node != null){
			int cmp = compare(element, node.element);
			if(cmp == 0){
				return node;
			}else if(cmp > 0){
				node = node.right;
			}else if(cmp < 0){
				node = node.left;
			}
		}
		return null;
	}
	
	private void elementIsNullCheck(E element){
		if(element == null){
			throw new IllegalArgumentException("element must not be null");
		}
	}
}
