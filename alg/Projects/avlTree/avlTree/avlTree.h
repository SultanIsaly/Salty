#ifndef AVL_HEAD_H_2016_04_08
#define AVL_HEAD_H_2016_04_08

#include "mm.h"
#include "iostream"
namespace good_morning
{
	template <class T, int(*Compare)(const T *pElement, const T* pElement2) >
	class CAVLTree
	{
		struct leaf
		{
			T* pData;
			leaf *pLeft;
			leaf *pRight;
			int height;

			leaf(T* data) : pData(data), pLeft(0), pRight(0), height(1) {}
			leaf() {}
		};

		leaf** m_pRoot;
		myManager::CMemoryManager<leaf> m_Memory;

		public:
			CAVLTree() : m_Memory(128, true), m_pRoot(0) {}

			virtual ~CAVLTree()
			{
				delete(m_pRoot); // +Автоматический clear в деструкторе менеджера.
			}

			bool add(T* pElement)
			{
				if (m_pRoot == 0) {
					leaf* pnew = m_Memory.newObject();
					*pnew = leaf(pElement);
					m_pRoot = new leaf*(pnew);
					return true;
				}
				else {
					bool added = false;
					insert(*m_pRoot, pElement, added);
					return  added;
				}
			}

			bool update(T* pElement)
			{
				if (m_pRoot == 0) {
					return 0;
				}
				else {
					return updateFrom(*m_pRoot, pElement);
				}
			}

			T* find(const T& element) // Переименовал аргумент тк он не pointer.
			{
				if (m_pRoot == 0) {
					return 0;
				}
				else {
					return findFrom(*m_pRoot, element);
				}
			}

			bool remove(const T& element)
			{
				if (m_pRoot == 0) {
					return false;
				}
				bool removed = false;
				removeFrom(*m_pRoot, element, removed);
				return removed;
			}

			void clear()
			{
				m_Memory.clear();
			}

		private:

			int 
			height (leaf* p) 
			{
				return p ? p->height : 0;
			}

			int 
			bfactor (leaf* p)
			{
				return height(p->right) - height(p->left);
			}

			void 
			fixheight (leaf* p) {
				int hl = height(p->pLeft);
				int hr = height(p->pRight);
				p->height = (hl > hr ? hl : hr) + 1;
			}

			void updateRoot(leaf* p) {
				if (*m_pRoot == p->pRight || *m_pRoot == p->pLeft) {
					*m_pRoot = p;
				}
			}

			leaf* 
			rotateRight (leaf* p) // правый поворот вокруг p
			{
				leaf* q = p->pLeft;
				p->pLeft = q->pRight;
				q->pRight = p;
				fixheight(p);
				fixheight(q);
				updateRoot(q);
				return q;
			}

			leaf* 
			rotateLeft(leaf* q) 
			{
				leaf* p = q->pRight;
				q->pRight = p->pLeft;
				p->pLeft = q;
				fixheight(q);
				fixheight(p);
				updateRoot(p);
				return p;
			}

			leaf* 
			balance (leaf* p) 
			{
				fixheight(p);
				if (bfactor(p) == 2) {
					if (bfactor(p->pRight) < 0) {
						p->pRight = rotateRight(p->pRight);
					}
					return rotateLeft(p);
				}
				if (bfactor(p) == -2) {
					if (bfactor(p->pLeft) > 0) {
						p->pLeft = rotateLeft(p->pLeft);
					}
					return rotateRight(p);
				}
				//балансировка не нужна
				return p;
			}

			node* insert(node* p, int k) // вставка ключа k в дерево с корнем p
			{
				if (!p) return new node(k);
				if (k<p->key)
					p->left = insert(p->left, k);
				else
					p->right = insert(p->right, k);
				return balance(p);
			}

			leaf* 
			insert (leaf* p, T* k) 
			{
				if (p == 0) {
					leaf* inLeaf = m_Memory.newObject();
					*inLeaf = leaf(k);
					return inLeaf;
				}
				bool comp(Compare(k, p->pData) < 0);
				if (comp) 
				{
					p->pLeft = insert(p->pLeft, k);
				}
				else
				{
					p->pRight = insert(p->pRight, k);
				}
				return balance(p);
			}

			leaf* 
			findmin (leaf* p) 
			{
				return p->pLeft ? findmin(p->pLeft) : p;
			}

			leaf* 
			removemin (leaf* p) 
			{
				if (p->pLeft == 0) {
					return p->pRight;
				}
				p->pLeft = removemin(p->pLeft);
				return balance(p);
			}

			leaf* 
			remove (leaf* p, const T& k) {
				if (p == 0) 
					return 0;
				bool compMore(Compare(&k, p->pData) < 0);
				bool compLess(Compare(&k, p->pData) > 0)
				if (compMore) 
				{
					p->pLeft = remove(p->pLeft, k);
				}
				else if (compLess) {
					p->pRight = remove(p->pRight, k);
				}
				else {
					/*node* q = p->left;
					node* r = p->right;
					delete p;
					if (!r) return q;
					node* min = findmin(r);
					min->right = removemin(r);
					min->left = q;
					return balance(min)*/
					bool right((*m_pRoot)->pRight == 0);
					bool left((*m_pRoot)->pLeft == 0)
					if (right && left) {
						delete m_pRoot;
						m_pRoot = 0;
						return 0;
					}
					leaf* q = p->pLeft;
					leaf* r = p->pRight;
					bool updateRootNeeded = false;
					if (*m_pRoot == p) {
						updateRootNeeded = true;
					}
					if (p != *m_pRoot) {
						m_Memory.deleteObject(p);
					}
					if (r == 0) {
						if (updateRootNeeded) {
							*m_pRoot = q;
						}
						return q;
					}
					leaf* min = findmin(r);
					min->pRight = removemin(r);
					min->pLeft = q;
					leaf* res = balance(min);
					if (updateRootNeeded) {
						*m_pRoot = res;
					}
					return res;
				}
				return balance(p);
			}

			T* findFrom(leaf* p, const T& element) {
				if (p == 0) {
					return 0;
				}
				if (Compare(&element, p->pData) < 0) {
					return findFrom(p->pLeft, element);
				}
				else if (Compare(&element, p->pData) > 0) {
					return findFrom(p->pRight, element);
				}
				else {
					return p->pData;
				}
			}

			bool updateFrom(leaf* p, T* pElement) {
				if (p == 0) {
					return false;
				}
				if (Compare(pElement, p->pData) < 0) {
					return updateFrom(p->pLeft, pElement);
				}
				else if (Compare(pElement, p->pData) > 0) {
					return updateFrom(p->pRight, pElement);
				}
				else {
					p->pData = pElement;
					return true;
				}
			}
		};
};

#endif // #define AVL_HEAD_H_2016_04_18
