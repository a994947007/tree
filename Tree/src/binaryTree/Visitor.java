package binaryTree;
	/**
	 * 访问器，用于访问内部元素，比如用来打印某个节点的数据
	 * @author 路遥
	 *
	 * @param <E>
	 */
	public abstract class Visitor<E> {
		boolean stop = false;
		public abstract boolean visit(E e); //访问后是否停止遍历
	}