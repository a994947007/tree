package RedBlackTree;

import java.util.Comparator;

import AVLTree.BBSTree;

public class RBTree<E> extends BBSTree<E> {
	private static final boolean RED = false;
	private static final boolean BLACK = true;
	
	public RBTree(){
		this(null);
	}
	
	public RBTree(Comparator<E> comparator){
		super(comparator);
	}
	
	@Override
	protected void afterAdd(Node<E> node) {
		Node<E> parent = node.parent;
		//添加的是根结点
		if(parent == null){
			black(node);
			return;
		}
		//添加的根结点是黑色
		if(isBlack(parent)) return;
		
		Node<E> uncle = parent.sibling();
		Node<E> grand = parent.parent;
		if(isRed(uncle)){		//B树上溢
			black(parent);
			black(uncle);
			red(grand);
			afterAdd(grand);
			return;
		}
		
		//叔父结点不是红色
		if(parent.isLeftChild()){
			if(node.isLeftChild()){	//LL
				black(parent);
				red(grand);
				rotateRight(grand);
			}else{	//LR
				black(node);
				red(grand);
				red(parent);
				rotateLeft(parent);
				rotateRight(grand);
			}
		}else{
			if(node.isRightChild()){		//RR
				black(parent);
				red(grand);
				rotateLeft(grand);
			}else{		//RL
				black(node);
				red(grand);
				red(parent);
				rotateRight(parent);
				rotateLeft(grand);
			}
		}
	}

	
	/**
	 * 将节点染成对应的颜色
	 * @param node
	 * @param color
	 * @return
	 */
	private Node<E> color(Node<E> node,boolean color){
		if(node == null){
			return node;
		}
		((RBNode<E>)node).color = color;
		return node;
	}
	
	private Node<E> red(Node<E> node){
		return color(node,RED);
	}
	
	private Node<E> black(Node<E> node){
		return color(node,BLACK);
	}
	
	@Override
	protected void afterRemove(Node<E> node,Node<E> replacement) {
		//被删除的结点只会发生在最后一层，即使不是叶子结点也没关系
		if(isRed(node)){
			return;
		}
		//用于取代的node结点是红色，自身肯定不会是红色，因为不可能Double red
		if(isRed(replacement)){
			black(replacement);
			return;
		}
		//来到这里一定是叶子结点
		//删除的结点是黑色
		Node<E> parent = node.parent;	//parent没有断
		if(parent == null){	//被删除的是根结点
			return;
		}
		boolean left = parent.left == null || node.isLeftChild();	
		//由于在儿叉排序树中会断掉left或者right，则根据left或者right是否为空的情况判定自身为left或right
		Node<E> sibling = left? parent.right : parent.left;
		if(left){	//被删除的结点在左边，兄弟节点在右边，左右操作对称
			if(isRed(sibling)){	//兄弟节点是红色，将其转换成兄弟节点是黑色的情况
				black(sibling);
				red(parent);
				rotateLeft(parent);
				sibling = parent.right;
			}
			//兄弟节点是黑色
			if(isBlack(sibling.left) && isBlack(sibling.right)){	//空节点也是black
				boolean parentBlack = isBlack(parent);
				black(parent);
				red(sibling);
				if(parentBlack){	//父节点为黑，下溢
					afterRemove(parent, null);
				}
			}else{	//兄弟节点至上有一个red
				if(isBlack(sibling.right)){	//左边如果是Black，则为LR，则转换成LL的情况
					rotateRight(sibling);
					sibling = parent.right;
				} 
				color(sibling,colorOf(parent));
				black(parent);
				black(sibling.right);
				rotateLeft(parent);
			}		
		}else{		//被删除结点在右边，兄弟节点在左边
			if(isRed(sibling)){	//兄弟节点是红色，将其转换成兄弟节点是黑色的情况
				black(sibling);
				red(parent);
				rotateRight(parent);
				sibling = parent.left;
			}
			//兄弟节点是黑色
			if(isBlack(sibling.left) && isBlack(sibling.right)){	//空节点也是black
				boolean parentBlack = isBlack(parent);
				black(parent);
				red(sibling);
				if(parentBlack){	//父节点为黑，下溢
					afterRemove(parent, null);
				}
			}else{	//兄弟节点至上有一个red
				if(isBlack(sibling.left)){	//左边如果是Black，则为LR，则转换成LL的情况
					rotateLeft(sibling);
					sibling = parent.left;
				}
				color(sibling,colorOf(parent));
				black(parent);
				black(sibling.left);
				rotateRight(parent);
			}
		}
	}
	
	/**
	 * 查看某个节点的颜色
	 * @param node
	 * @return
	 */
	private boolean colorOf(Node<E> node){
		//空节点默认为黑色
		return node == null?BLACK:((RBNode<E>)node).color;
	}
	
	/**
	 * 是否为黑色节点
	 * @param node
	 * @return
	 */
	private boolean isBlack(Node<E> node){
		return colorOf(node) == BLACK;
	}
	
	@Override
	protected Node<E> createNode(E element,Node<E> parent) {
		return new RBNode<>(element,parent);
	}
	
	/**
	 * 是否为红色节点
	 * @param node
	 * @return
	 */
	private boolean isRed(Node<E> node){
		return colorOf(node) == RED;
	}
	
	private static class RBNode<E> extends Node<E>{
		boolean color = RED;
		public RBNode(E element,Node<E> parent){
			super(element,parent);
		}
	}

	@Override
	protected void afterRotateLeft(Node<E> grand,Node<E> parent,Node<E> child) {
	}
	
	@Override
	protected void afterRotateRight(Node<E> grand,Node<E> parent,Node<E> child) {

	}
}
