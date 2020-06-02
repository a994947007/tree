package AVLTree;

import java.util.Comparator;

import BinarySearchTree.BSTreeImpl;

/**
 * 平衡二叉搜索树
 * @author 路遥
 *
 */
public abstract class BBSTree<E> extends BSTreeImpl<E>{
	public BBSTree(){
		this(null);
	}
	
	public BBSTree(Comparator<E> comparator){
		super(comparator);
	}
	
	/**
	 * 左旋
	 * @param node
	 */
	protected void rotateLeft(Node<E> grand){
		Node<E> parent = grand.right;
		Node<E> child = parent.left;
		grand.right = child;
		parent.left = grand;
		parent.parent = grand.parent;
		if(grand.isLeftChild()){
			grand.parent.left = parent;
		}else if(grand.isRightChild()){
			grand.parent.right = parent;
		}else{	//要删除的结点为根结点
			root = parent;
		}
		if(child != null)child.parent = grand;
		grand.parent = parent;
		
		afterRotateRight(grand, parent, child);
	}
	
	
	protected abstract void afterRotateLeft(Node<E> grand,Node<E> parent,Node<E> child);
	protected abstract void afterRotateRight(Node<E> grand,Node<E> parent,Node<E> child);
	
	/**
	 * 右旋
	 * @param node
	 */
	protected void rotateRight(Node<E> grand){
		Node<E> parent = grand.left;
		Node<E> child = parent.right;
		parent.right = grand;
		grand.left = child;
		parent.parent = grand.parent;
		if(grand.isLeftChild()){
			grand.parent.left = parent;
		}else if(grand.isRightChild()){
			grand.parent.right = parent;
		}else{	//要删除的结点为根结点
			root = parent;
		}
		if(child != null) child.parent = grand;
		grand.parent = parent;
		afterRotateRight(grand, parent, child);
	}
}
