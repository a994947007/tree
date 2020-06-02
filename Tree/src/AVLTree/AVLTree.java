package AVLTree;

import java.util.Comparator;


/**
 * 任意结点的左右子树高度差不超过1
 * 添加后失衡调整、删除后失衡调整，添加、删除后，父节点永远不会失衡，失衡只能是祖先节点
 * @author 路遥
 *
 * @param <E>
 */
public class AVLTree<E> extends BBSTree<E>{
	
	public AVLTree(){
		this(null);
	}
	
	public AVLTree(Comparator<E> comparator){
		super(comparator);
	}

	@Override
	public void add(E element) {
		super.add(element);	
	}
	@Override
	public void remove(E element) {
		super.remove(element);
	}
	
	@Override
	protected void afterAdd(Node<E> node){
		while((node = node.parent) != null){
			if(isBanlance(node)){
				//更新高度
				updateHeight(node);
			}else{
				//恢复平衡
				rebalance(node);
				break;	//调整后直接break，调整后的子树和添加结点前子树高度不变， 因此上层结点不会失衡
			}
		}
	}
	
	@Override
	protected void afterRemove(Node<E> node,Node<E> replacement){
		while((node = node.parent) != null){
			if(isBanlance(node)){
				updateHeight(node);
			}else{
				rebalance(node);	//这里不需要break，因为在调整后，可能会导致上层祖父结点失衡
			}
		}
	}
	
	/**
	 * 恢复平衡，不平衡的结点一定是添加后的祖先结点
	 * @param grand
	 */
	private void rebalance(Node<E> grand){
		Node<E> parent = ((AVLNode<E>)grand).tallerChild();
		Node<E> node = ((AVLNode<E>)parent).tallerChild();
		if(parent.isLeftChild()){
			if(node.isLeftChild()){	//LL
				rotateRight(grand);
			}else{ //LR
				rotateLeft(parent);
				rotateRight(grand);
			}
		}else{
			if(node.isLeftChild()){ //RL
				rotateRight(parent);
				rotateLeft(grand);
			}else{ //RR
				rotateLeft(grand);
			}
		}
	}
	
	/***另外一种调整思路***/
	@SuppressWarnings("unused")
	private void rebalance2(Node<E> grand){
		Node<E> parent = ((AVLNode<E>)grand).tallerChild();
		Node<E> node = ((AVLNode<E>)parent).tallerChild();	
		if(parent.isLeftChild()){
			if(node.isLeftChild()){	//LL
				rotate(grand, node.left, node, node.right, parent, parent.right, grand, grand.right);
			}else{ //LR
				rotate(grand, parent.left, parent, node.left, node, node.right, grand, grand.right);
			}
		}else{
			if(node.isLeftChild()){ //RL
				rotate(grand, grand.left, grand, node.left, node, node.right, parent, parent.right);
			}else{ //RR
				rotate(grand, grand.left, grand, parent.left, parent, node.left, node, node.right);
			}
		}
	}
	
	private void rotate(Node<E> r,		//子树的根结点
			Node<E> a,Node<E> b,Node<E> c,
			Node<E> d,
			Node<E> e,Node<E> f,Node<E> g){
		d.parent = r.parent;
		if(r.isLeftChild()){
			r.parent.left = d;
		}else if(r.isRightChild()){
			r.parent.right = d;
		}else{
			root = d;
		}
		
		b.left = a;
		if(a != null)a.parent = b;
		b.right = c;
		if(c != null)c.parent = b;
		updateHeight(b);
		
		f.left = e;
		if(e != null)a.parent = f;
		f.right = g;
		if(g != null)g.parent = f;
		updateHeight(f);	
		
		d.left = b;
		b.parent = d;
		d.right = f;
		f.parent = d;
		updateHeight(d);	
	}
	
	/********************/

	@Override
	protected Node<E> createNode(E element,Node<E> parent) {
		return new AVLNode<>(element, parent);
	}
	
	/**
	 * 是否平衡
	 * @param node
	 * @return
	 */
	private boolean isBanlance(Node<E> node){
		return Math.abs(((AVLNode<E>)node).balanceFactor()) <= 1;
	}
	
	/**
	 * 更新高度
	 * @param node
	 */
	private void updateHeight(Node<E> node){
		AVLNode<E> avlNode = (AVLNode<E>)node;
		avlNode.updateHeight();
	}
	
	protected static class AVLNode<E> extends Node<E>{
		public int height = 1;
		public AVLNode(E element, Node<E> parent) {
			super(element, parent);
		}
		/**
		 * 获取平衡因子 
		 * @return
		 */
		public int balanceFactor(){
			int leftHeight = left == null?0:((AVLNode<E>)left).height;
			int rightHeight = right == null?0:((AVLNode<E>)right).height;
			return leftHeight - rightHeight;
		}
		
		public void updateHeight(){
			int leftHeight = left == null?0:((AVLNode<E>)left).height;
			int rightHeight = right == null?0:((AVLNode<E>)right).height;
			this.height = (leftHeight > rightHeight?leftHeight:rightHeight) + 1;
		}
		
		/**
		 * 获取左右子树中高的结点
		 * @return
		 */
		public AVLNode<E> tallerChild(){
			int leftHeight = left == null?0:((AVLNode<E>)left).height;
			int rightHeight = right == null?0:((AVLNode<E>)right).height;
			return (AVLNode<E>)(leftHeight > rightHeight?left:right);
		}
	}

	@Override
	protected void afterRotateLeft(binaryTree.BinaryTree.Node<E> grand,
			binaryTree.BinaryTree.Node<E> parent,
			binaryTree.BinaryTree.Node<E> child) {
		updateHeight(grand);
		updateHeight(parent);
	}
	
	@Override
	protected void afterRotateRight(binaryTree.BinaryTree.Node<E> grand,
			binaryTree.BinaryTree.Node<E> parent,
			binaryTree.BinaryTree.Node<E> child) {
		updateHeight(grand);
		updateHeight(parent);
	}
}
