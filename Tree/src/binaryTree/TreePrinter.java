package binaryTree;


public interface TreePrinter<E> {
	//先序
	void preTraver(Visitor<E> visitor);
	//中序
	void inTraver(Visitor<E> visitor);
	//后序
	void postTraver(Visitor<E> visitor);
	//层序
	void levelTraver(Visitor<E> visitor);
	//用栈先序
	void preTraverWithStack(Visitor<E> visitor);
	//用栈中序
	void inTraverWithStack(Visitor<E> visitor);
	//用栈后序
	void postTraverWithStack(Visitor<E> visitor);
}
