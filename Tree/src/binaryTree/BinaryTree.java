package binaryTree;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;


public class BinaryTree<E> implements TreePrinter<E>  {
	protected int size;
	protected Node<E> root;
	public BinaryTree(){
		this.size = 0;
	}
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return root == null;
	}

	public void clear() {
		root = null;
		size = 0;
	}
	
	/**
	 * 将整棵二叉树翻转，左子树变成右子树，右子树变成左子树
	 */
	public void reverse(){
		reverse(root);
	}
	
	private void reverse(Node<E> root){
		if(root == null){
			return;
		}
		Node<E> tmp = root.left;
		root.left = root.right;
		root.right = tmp;
		reverse(root.left);
		reverse(root.right);
	}
	
	protected Node<E> createNode(E element,Node<E> parent){
		return new Node<>(element,parent);
	}
	
	/**
	 * 获取前驱结点
	 * @param node
	 * @return
	 */
	protected Node<E> predesessor(Node<E> node){
		if(node == null){
			return null;
		}
		//前驱结点在左子树
		Node<E> p = node.left;
		if(p != null){
			while(p.right != null){
				p = p.right;
			}
			return p;
		}
		//前驱结点在祖父结点
		while(node.parent != null && node.parent.left == node){
			node = node.parent;
		}
		//当前结点为父节点的右结点，或者父节点为空，均返回父节点
		return node.parent;
	}
	
	/**
	 * 获取后继结点
	 * @param node
	 * @return
	 */
	protected Node<E> successor(Node<E> node){
		if(node == null){
			return null;
		}
		//前驱结点在右子树
		Node<E> p = node.right;
		if(p != null){
			while(p.left != null){
				p = p.left;
			}
			return p;
		}
		//后继结点在祖父结点
		while(node.parent != null && node == node.parent.right){
			node = node.parent;
		}
		//能来到这里，当前结点为父节点的左结点，或者父节点为空，两者都可返回父节点
		return node.parent;
	}	

	/**
	 * 先序遍历
	 * @param visitor
	 */
	@Override
	public void preTraver(Visitor<E> visitor) {
		if(visitor == null){
			return;
		}
		preTraver(root,visitor);
	}
	
	public void preTraver(Node<E> root,Visitor<E> visitor){
		if(root == null || visitor.stop){
			return;
		}
		//每次需要更新stop的状态，因为在某次visit访问后可能需要停止访问
		visitor.stop = visitor.visit(root.element);
		preTraver(root.left,visitor);
		preTraver(root.right,visitor);
	}
	
	/**
	 * 中序遍历
	 * @param visitor
	 */
	@Override
	public void inTraver(Visitor<E> visitor) {
		if(visitor == null){
			return;
		}
		inTraver(root,visitor);
	}
	
	public void inTraver(Node<E> root,Visitor<E> visitor){
		if(root == null || visitor.stop){
			return;
		}
		inTraver(root.left,visitor);
		visitor.stop = visitor.visit(root.element);
		if(visitor.stop) return;
		inTraver(root.right,visitor);	
	}
	
	/**
	 * 后序遍历
	 * @param visitor
	 */
	@Override
	public void postTraver(Visitor<E> visitor) {
		if(visitor == null){
			return;
		}
		postTraver(root,visitor);
	}

	public void postTraver(Node<E> root,Visitor<E> visitor){
		if(root == null || visitor.stop){
			return;
		}
		postTraver(root.left,visitor);
		postTraver(root.right,visitor);	
		visitor.stop = visitor.visit(root.element);
		if(visitor.stop) return;
	}
	
	/**
	 * 层序遍历
	 */
	@Override
	public void levelTraver(Visitor<E> visitor) {
		if(root == null || visitor == null){
			return;
		}
		Queue<Node<E>> que = new LinkedList<Node<E>>();
		que.offer(root);
		while(!que.isEmpty()){
			Node<E> node = que.poll();
			if(node == null){
				break;
			}
			visitor.stop = visitor.visit(node.element);
			if(visitor.stop){
				return;
			}
			if(node.left != null) que.offer(node.left);
			if(node.right != null) que.offer(node.right);
		}
	}
	
	/**
	 * 用栈先序遍历
	 */
	@Override
	public void preTraverWithStack(Visitor<E> visitor) {
		if(visitor == null || root == null){
			return;
		}
		Stack<Node<E>> stack = new Stack<Node<E>>();
		stack.push(root);
		while(!stack.isEmpty()){
			Node<E> node = stack.pop();
			visitor.stop = visitor.visit(node.element);
			if(visitor.stop){
				return;
			}
			if(node.right != null) stack.push(node.right);
			if(node.left != null) stack.push(node.left);
		}
	}
	
	/**
	 * 用栈中序遍历
	 */
	@Override
	public void inTraverWithStack(Visitor<E> visitor) {
		if(visitor == null || root == null){
			return;
		}
		Stack<Node<E>> stack = new Stack<Node<E>>();
		stack.push(root);
		Node<E> p = root;
		while(!stack.isEmpty()){
			while(p.left != null){
				stack.push(p.left);
				p = p.left;
			}
			while(!stack.isEmpty()){
				Node<E> node = stack.pop();
				if(visitor.stop = visitor.visit(node.element)){
					return;
				}
				if(node.right != null){
					stack.push(node.right);
					p = node.right;
					break;
				}
			}
		}
	}
	
	/**
	 * 用栈后序遍历
	 */
	@Override
	public void postTraverWithStack(Visitor<E> visitor) {
		if(visitor == null || root == null){
			return;
		}
		Stack<Node<E>> stack1 = new Stack<Node<E>>();
		Stack<Node<E>> stack2 = new Stack<Node<E>>();
		stack1.push(root);
		while(!stack1.isEmpty()){
			Node<E> node = stack1.pop();
			stack2.push(node);
			if(node.left != null)stack1.push(node.left);
			if(node.right != null)stack1.push(node.right);
		}
		while(!stack2.isEmpty()){
			if(visitor.stop = visitor.visit(stack2.pop().element)){
				return;
			}
		}
	}
	
	/**
	 * 每访问一完层，hight加1，下一层结点的数量对应访问该层最后一个结点后队列的size
	 */
	public int hight(){
		int hight = 0;
		if(root == null){
			return hight;
		}
		int levelSize = 1;
		Queue<Node<E>> que = new LinkedList<Node<E>>();
		que.offer(root);
		while(!que.isEmpty()){
			Node<E> node = que.poll();
			levelSize --;
			if(node.left != null) que.offer(node.left);
			if(node.right != null) que.offer(node.right);
			if(levelSize == 0){	//0表示已经访问完毕
				levelSize = que.size();
				hight ++;
			}
		}		
		return hight;	
	}
	
	/**
	 * 层序遍历二叉树，每一层必须严格遵守完全二叉树的定义
	 */
	public boolean isComplite() {
		if(root == null) return false;
		Queue<Node<E>> que = new LinkedList<Node<E>>();
		que.offer(root);
		boolean leaf = false;
		while(!que.isEmpty()){
			Node<E> node = que.poll();
			if(leaf & !node.isLeaf()){
				return false;
			}
			if(node.left != null){
				que.offer(node.left);
			}else if(node.right != null){
				return false;
			}
			if(node.right != null){
				 que.offer(node.right);
			}else{		
				//left==null&&right==null或者 left!=null&&right==null的情况，后面的结点一定都是叶子结点
				leaf = true;
			}
		}
		return true;
	}
	
	protected static class Node<E>{
		public E element;
		public Node<E> left;
		public Node<E> right;
		public Node<E> parent;
		public Node(E element,Node<E> parent){
			this.element = element;
			this.parent = parent;
		}
		
		public boolean isLeaf(){
			return left == null && right == null;
		}
		
		public boolean hasTwoChildren(){
			if(left != null && right != null){
				return true;
			}
			return false;
		}
		
		public boolean isLeftChild(){
			return parent != null && this == parent.left;
		}
		
		public boolean isRightChild(){
			return parent != null && this == parent.right;
		}
		
		/**
		 * 获取兄弟节点
		 * @return
		 */
		public Node<E> sibling(){
			if(parent == null) return null;
			if(isLeftChild()){
				return parent.right;
			}else{
				return parent.left;
			}
		}
	}
}
