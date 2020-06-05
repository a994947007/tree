package BTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class BTree<E> {
	private static final int DEFUALT_DEGREE = 4;		//默认阶数
	private int degree;					//阶数
	private int minElementSize;			//最小元素个数
	private int maxElementSize;			//最大元素个数
	private int maxChildrenSize;		//最大孩子数
	private BNode root;					//根结点
	private int size;					//元素个数
	private Comparator<E> comparator;
	public BTree(){
		this.degree = DEFUALT_DEGREE;
		this.minElementSize = (int) (Math.ceil(degree / 2.0) - 1);
		this.maxElementSize = degree - 1;
		this.maxChildrenSize = degree;
	}

	public BTree(int degree){
		this.degree = degree;
	}

	public BTree(Comparator<E> comparator){
		this();
		this.comparator = comparator;
	}

	public BTree(int degree,Comparator<E> comparator){
		this(degree);
		this.comparator = comparator;
	}

	/**
	 * 是否包含元素element
	 * @param element
	 * @return
	 */
	public boolean contains(E element){
		return node(element) != null;
	}
	
	/**
	 * 从B树中搜索元素
	 * @param element
	 */
	private BNode node(E element){
		BNode node = root;
		while(node != null){
			if(compare(element,node.getMaxElement()) > 0){			//比节点中最大的元素还要大
				node = node.getRightestChild();
			}else{
				SearchResult rs = node.search(element);
				if(rs.searchStatus){
					return node;
				}else{
					if(node.getChildNum() == 0){
						break;
					}
					node = node.getChild(rs.index);
				}
			}
		}
		return null;
	}
	
	/**
	 * 添加一个元素到B树中
	 * 从根结点向下搜索，直到找到可插入的叶子结点
	 * @param element
	 */
	public boolean add(E element){
		if(root == null){
			root = new BNode(null);
			root.add(element);
			return true;
		}
		BNode node = root;
		BNode parent = root;
		while(node != null){
			parent = node;
			if(parent.getChildNum() == 0){	//叶子结点直接跳出进行添加
				break;
			}
			if(compare(element, node.getMaxElement())  > 0){	//比结点中最大的元素还大
				node = node.getChild(node.getChildNum() - 1);
			}else{
				SearchResult sr = node.search(element);
				if(sr.searchStatus){	//不添加重复元素
					return false;
				}
				node = node.getChild(sr.index);
			}
		}
		parent.add(element);
		if(parent.getElementNum() > this.maxElementSize){		//添加之后判断是否需要分裂
			splitNode(parent);
		}
		size++;
		return true;
	}

	/**
	 * 分裂结点
	 * @param node
	 */
	private void splitNode(BNode node){
		int splitIndex = node.getElementNum() / 2;
		E splitElement = node.getElement(splitIndex);
		BNode left = new BNode();		//分裂左结点
		for(int i = 0;i < splitIndex; i++){
			left.add(node.getElement(i));
		}
		if(node.getChildNum() > 0){
			left.addChildren(node.childNodes.subList(0, splitIndex + 1)); //subList参数，from,to，不包含to
		}
		BNode right = new BNode();		//分裂右结点
		for (int i = splitIndex + 1; i < node.getElementNum(); i++) {
			right.add(node.getElement(i));
		}
		if(node.getChildNum() > 0){
			right.addChildren(node.childNodes.subList(splitIndex + 1, node.getChildNum())); //subList参数，from,to，不包含to
		}
		if(node.parent != null){
			BNode parent = node.parent;
			parent.add(splitElement);
			parent.removeChild(node);
			parent.add(left);
			parent.add(right);
			if(parent.getElementNum() > maxElementSize){	//父节点上溢，继续递归操作	
				splitNode(parent);
			}
		}else{	//到达根结点
			root = new BNode();
			root.add(splitElement);
			root.add(left);
			root.add(right);
		}
	}

	/**
	 * 从B树中删除一个元素
	 * @return
	 */
	public boolean remove(E element){
		BNode node = node(element);
		if(node == null){
			return false;
		}
		if(node.getChildNum() > 0){		//不在叶子结点
			E replacement = predesessor(element);
			node.removeElement(element);
			node.add(replacement);
			element = replacement;		//删除被替换结点
		}		
		node = node(element);	//在叶子结点中删除
		if(node.parent == null && node.getElementNum() == 1){
				root = null;				
		}else{		
			node.removeElement(element);
			if(node.getElementNum() < minElementSize && node.parent != null){		//非根结点叶子下溢
				combine(node);
			}
		}
		size --;
		return true;
	}
	
	/**
	 * 下溢合并
	 * @param node 发生下溢的结点
	 */
	private void combine(BNode node){
		BNode parent = node.parent;
		int index = parent.childNodes.indexOf(node);
		int leftSiblingIndex = index - 1;
		int rightSiblingIndex = index + 1;
		BNode sibling = null;
		if(leftSiblingIndex >= 0){	//左孩兄弟存在,从左兄弟中借一个结点
			if((sibling = parent.getChild(leftSiblingIndex)).getElementNum() > minElementSize){
				E siblingMaxElement = sibling.getMaxElement();
				sibling.removeElement(siblingMaxElement);
				if(sibling.childNodes.size() > 0){
					BNode siblingRightestChild = sibling.getRightestChild();
					sibling.removeChild(siblingRightestChild);
					node.add(siblingRightestChild);
				}
				node.add(parent.getElement(leftSiblingIndex));
				parent.elements.remove(leftSiblingIndex);
				parent.add(siblingMaxElement);
				return;
			}
		}
		if(rightSiblingIndex < parent.getChildNum()){	//必有一个条件成立
			if((sibling = parent.getChild(rightSiblingIndex)).getElementNum()> minElementSize){
				E siblingMinElement = sibling.getElement(0);
				sibling.removeElement(siblingMinElement);
				if(sibling.childNodes.size() > 0){
					BNode siblingLeftestChild = sibling.getChild(0);
					sibling.removeChild(siblingLeftestChild);
					node.add(siblingLeftestChild);
				}
				node.add(parent.getElement(rightSiblingIndex - 1));
				parent.elements.remove(rightSiblingIndex - 1);
				parent.add(siblingMinElement);	
				return;
			}
		}
		int parentRemoveIndex = 0;
		if(leftSiblingIndex >= 0){	//上面没有借到，从父节点拿一个下来合并
			sibling = parent.getChild(leftSiblingIndex);
			parentRemoveIndex = index - 1;
		}else if(rightSiblingIndex < parent.getChildNum()){
			sibling = parent.getChild(rightSiblingIndex);
			parentRemoveIndex = index;
		}
		node.addElements(sibling.elements);
		node.addChildren(sibling.childNodes);
		node.add(parent.getElement(parentRemoveIndex));
		parent.removeChild(sibling);
		parent.elements.remove(parentRemoveIndex);
		if(parent.parent == null && parent.getElementNum() == 0){
			node.parent = null;
			root = node;
		}else if(parent.getElementNum() < minElementSize && parent.parent != null){
			combine(parent);
		}
	}
	
	/**
	 * 获取前驱元素
	 * @param element
	 * @return
	 */
	private E predesessor(E element){
		BNode node = node(element);
		int index = node.search(element).index;
		if(node.getChildNum() > 0){		//孩子存在，则前驱元素在孩子的最右
			BNode p = node.getChild(index);
			while(p.getChildNum() > 0){
				p = p.getRightestChild();
			}
			return p.getMaxElement();
		}else{							//孩子不存在，前驱元素在自身或者父节点中
			if(index == 0){				//前驱元素在父节点中
				BNode parent = node.parent;
				if(parent == null){	
					return null;
				}else{	
					int i = 0;
					while((i = parent.search(element).index) == 0 && parent.parent != null){	//如果是在父节点的最左边，继续往上找
						parent = parent.parent;
					}
					if(i == 0){
						return null;
					}else{
						return parent.getElement(i - 1);
					}
				}
			}else{
				return node.getElement(index - 1);
			}
		}
	}
	
	/**
	 * 获取后继元素
	 * @param element
	 * @return
	 */
	@SuppressWarnings("unused")
	private E successor(E element){
		BNode node = node(element);
		int index = node.search(element).index;
		if(node.getChildNum() > 0){		//孩子存在，则后继在孩子的最左
			BNode p = null;
			if(compare(element,node.getMaxElement())> 0){
				p = node.getRightestChild();
			}else{
				p = node.getChild(index);
			}
			while(p.getChildNum() > 0){
				p = p.getChild(0);
			}
			return p.getElement(0);
		}else{			//后继在父节点或者自身中
			if(index == node.getElementNum() - 1){
				BNode parent = node.parent;
				if(parent == null){
					return null;
				}else{
					int i = 0;
					while(compare(element,parent.getMaxElement()) > 0 && parent.parent != null){
						parent = parent.parent;
					}
					if(compare(element,parent.getMaxElement())>0){
						return null;
					}else{
						return parent.getElement(parent.search(element).index);
					}
				}
			}else{
				return node.getElement(index + 1);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private int compare(E e,E e2){
		if(comparator != null){
			return comparator.compare(e, e2);
		}else{
			return ((Comparable<E>)e).compareTo(e2);	
		}
	}


	/**
	 * 用于保存查询结果
	 * 查询成功，searchStatus为true，index为对应的元素的位置
	 * 查询失败，searchStatus为false，index为可插入的元素的位置
	 */
	private class SearchResult{
		public boolean searchStatus;
		public int index;
		public SearchResult(boolean searchStatus,int index){
			this.searchStatus = searchStatus;
			this.index = index;
		}
	}

	private class BNode{
		public List<E> elements;			//元素集合
		public List<BNode> childNodes;		//孩子集合
		public BNode parent;				//父节点
		private Comparator<BNode> comparator;	//用于比较两个BNode结点的大小

		public BNode(){
			elements = new ArrayList<E>(maxElementSize);
			childNodes = new ArrayList<BNode>(maxChildrenSize);
			comparator = new Comparator<BNode>() {
				@Override
				public int compare(BNode o1, BNode o2) {
					return BTree.this.compare(o1.getElement(0), o2.getElement(0));
				}
			};
		}
		public BNode(BNode parent){
			this();
			this.parent = parent;
		}

		/**
		 * 根据下标获取元素
		 * @param index
		 * @return
		 */
		public E getElement(int index){
			return elements.get(index);
		}

		/**
		 * 根据下标获取孩子节点
		 * @param index
		 * @return
		 */
		public BNode getChild(int index){
			return childNodes.get(index);
		}

		/**
		 * //批量添加孩子
		 * @param children
		 */
		public void addChildren(List<BNode> children){
			for(BNode node:children){
				node.parent = this;
			}
			childNodes.addAll(children);
			Collections.sort(childNodes,comparator);
		}
		
		public void addElements(List<E> elements){
			this.elements.addAll(elements);
			Collections.sort(this.elements,new Comparator<E>() {
				@Override
				public int compare(E o1, E o2) {
					return BTree.this.compare(o1, o2);
				}
			});
		}

		/**
		 * 在当前节点中查找元素对应的位置，成功找到则返回元素，否则返回元素该插入的位置
		 * @param element
		 * @return
		 */
		public SearchResult search(E element){
			int low = 0;
			int high = elements.size() - 1;
			int mid = 0;
			while(low <= high){
				mid = (low + high) >>> 1;
				E midElement = elements.get(mid);
				int cmp = BTree.this.compare(element, midElement);
				if(cmp < 0){
					high = mid - 1;
				}else if(cmp > 0){
					low = mid + 1;
				}else{
					return new SearchResult(true,mid);
				}
			}
			return new SearchResult(false,mid);
		}

		/**
		 * 添加一个元素
		 * @param element
		 * @return 如果元素存在，返回false，否则返回true
		 */
		public boolean add(E element){
			SearchResult result = search(element);
			if(result.searchStatus){
				return false;
			}else{
				if(elements.size() == 0){			//结点中没有元素无法比较，直接添加
					this.elements.add(0, element);
					return true;
				}
				int index = search(element).index;
				if(compare(element, elements.get(index)) > 0){
					index ++;
				}
				this.elements.add(index, element);
				return true;
			}
		}

		/**
		 * 添加一个孩子
		 * @param child
		 */
		public void add(BNode child){
			child.parent = this;
			childNodes.add(child);
			Collections.sort(childNodes,comparator);
		}

		/**
		 * 删除孩子节点
		 * @param child
		 */
		public boolean removeChild(BNode child){
			return childNodes.remove(child);
		}
		
		/**
		 * 删除节点中的元素
		 */
		public boolean removeElement(E element){
			SearchResult rs = search(element);
			if(rs.searchStatus){
				elements.remove(rs.index);
				return true;
			}else{
				return false;
			}
		}

		/**
		 * 获取元素个数
		 * @return
		 */
		public int getElementNum(){
			return elements.size();
		}

		/**
		 * 获取孩子个数
		 * @return
		 */
		public int getChildNum(){
			return childNodes.size();
		}

		/**
		 * 获取值最大的节点
		 * @return
		 */
		public E getMaxElement(){
			return elements.get(elements.size() - 1);
		}
		
		/**
		 * 获取最右边的节点
		 * @return
		 */
		public BNode getRightestChild(){
			if(childNodes.size() > 0){
				return childNodes.get(childNodes.size() - 1);
			}
			return null;
		}
	}
	
	/**
	 * 用于打印测试
	 * @author 路遥
	 *
	 */
    public class TreePrinter   
    {  
  
        public  String getString(BTree<E> tree)   
        {  
            if (tree.root == null) return "Tree has no nodes.";  
            return getString(tree.root, "", true);  
        }  
  
        private String getString(BNode node, String prefix, boolean isTail)   
        {  
            StringBuilder builder = new StringBuilder();  
  
            builder.append(prefix).append((isTail ? "└── " : "├── "));  
            for (int i = 0; i < node.getElementNum(); i++) {  
                E value = node.getElement(i);  
                builder.append(value);  
                if (i < node.getElementNum() - 1) builder.append(", ");  
            }  
            builder.append("\n");  
  
            if (node.childNodes != null) {  
                for (int i = 0; i < node.getChildNum() - 1; i++) {  
                    BNode obj = node.getChild(i);  
                    builder.append(getString(obj, prefix + (isTail ? "    " : "│   "), false));  
                }  
                if (node.getChildNum() >= 1) {  
                    BNode obj = node.getChild(node.getChildNum() - 1);  
                    builder.append(getString(obj, prefix + (isTail ? "    " : "│   "), true));  
                }  
            }  
  
            return builder.toString();  
        }  
    }  
}
