package BinarySearchTree;

public interface BSTree<E> {
	//添加元素
	void add(E element);
	//删除元素
	void remove(E element);
	//是否包含当前元素
	boolean contains(E element);
}
