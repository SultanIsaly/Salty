#ifndef DOUBLYLINKEDLIST_H_2016_04_12
#define DOUBLYLINKEDLIST_H_2016_04_12

#include "Exception.h"

namespace magic_morning
{
	template<class T>
	class CDoublyLinkedList
	{
		leaf* pBegin, *pEnd;

		struct leaf
		{
			T data;
			leaf * pnext, *pprev;
			leaf(T& _data, leaf * _pprev = 0, leaf * _pnext = 0)
				: data(_data)
				, pprev(_pprev)
				, pnext(_pnext)
			{}
		};

	public:
		class CIterator
		{
			//храним голову списка, если мы находимся перед началом
			leaf* pItBegin;
			// храним текущее положение
			leaf* pItCurrent;
			//храним конец списка, если мы находимся после конца
			leaf* pItEnd;

		public:
			CIterator ()
				: pItBegin(0)
				, pItCurrent(0)
				, pItEnd(0)
			{}

			CIterator (leaf *p)
				: pItBegin(0)
				, pItCurrent(p)
				, pItEnd(0)
			{}

			CIterator (const CIterator &src)
				: pItBegin(src.pItBegin)
				, pItCurrent(src.pItCurrent)
				, pItEnd(src.pItEnd)
			{}

			~CIterator ()
			{}

			CIterator& 
			operator = (const CIterator&  src)
			{
				if (this == &src)
				{
					return *this;
				}
				pItBegin = src.pItBegin;
				pItCurrent = src.pItCurrent;
				pItEnd = src.pItEnd;
				return *this;
			}

			bool
			operator != (const CIterator&  it) const
			{
				bool equalityCurrentIterator(pItCurrent != it.pItCurrent);
				bool equalityBeginIterator(pItBegin != 0 && pItBegin != it.pItBegin);
				bool equalityEndIterator(pItEnd != 0 && pItEnd != it.pItEnd);
				return (equalityCurrentIterator || equalityBeginIterator || equalityEndIterator);
			}

			void 
			operator++ ()
			{
				if (!isValid())
				{
					MyException("Iterator is invalid\n");
				}
				if (pItBegin != 0)
				{
					pItCurrent = pItBegin;
					pItBegin = 0;
					return;
				}
				if (pItEnd != 0)
				{
					MyException("Iterator is out of the list\n");
				}
				//если находимся в конце
				if (pItCurrent->pnext == 0)
				{
					setLeafPostEnd(pItCurrent);
					return;
				}
				pItCurrent = pItCurrent->pnext;
			}

			void 
			operator--()
			{
				if (!isValid())
				{
					MyException("Iterator is invalid\n");
				}
				if (pItEnd != 0)
				{
					pItCurrent = pItEnd;
					pItEnd = 0;
					return;
				}
				if (pItBegin != 0)
				{
					MyException("Iterator is out of the list\n");
				}
				//если находимся в начале
				if (pItCurrent->pprev == 0)
				{
					setLeafPreBegin(pItCurrent);
					return;
				}
				pItCurrent = pItCurrent->pprev;
			}

			T& 
			getData ()
			{
				if (!isValid())
				{
					MyException("Iterator is invalid\n");
				}
				if (pItCurrent == 0)
				{
					MyException("Iterator is out of the list\n");
				}
				return pItCurrent->data;
			}

			T&
			operator* ()
			{
				return getData();
			}

			leaf* 
			getLeaf ()
			{
				return pItCurrent;
			}

			void 
			setLeaf (leaf* p)
			{
				pItBegin = 0;
				pItCurrent = p;
				pItEnd = 0;
			}

			void
			setLeafPreBegin (leaf* p)
			{
				pItBegin = p;
				pItCurrent = 0;
				pItEnd = 0;
			}

			void 
			setLeafPostEnd (leaf* p)
			{
				pItBegin = 0;
				pItCurrent = 0;
				pItEnd = p;
			}

			bool
			isValid ()
			{
				return (pItCurrent != 0 || pItBegin != 0 || pItEnd != 0);
			}

			bool
			isPreBegin ()
			{
				if (pItBegin != 0)
				{
					return true;
				}
				return false;
			}

		};


	public:

		CDoublyLinkedList ()
			: pItBegin(0)
			, pItEnd(0)
		{};

		virtual ~CDoublyLinkedList ()
		{
			clear();
		};

		void
		pushBack (T& data)
		{
			leaf* newLeaf = new leaf(data, pItEnd, 0);
			if (pItBegin == 0)
			{
				pItBegin = newLeaf;
				pItEnd = newLeaf;
				return;
			}
			pItEnd->pnext = newLeaf;
			pItEnd = newLeaf;
		}

		T
		popBack ()
		{
			if (pItBegin == 0)
			{
				MyException("The list is empty\n");
			}
			T value = pItEnd->data;
			leaf* pdelete = pItEnd;
			pItEnd = pItEnd->pprev;
			pItEnd->pnext = 0;
			if (pItBegin == pdelete)
			{
				pItBegin = 0;
			}
			delete pdelete;
			return value;
		}

		void 
		pushFront (T& data)
		{
			leaf* newLeaf = new leaf(data, 0, pItBegin);
			if (pItBegin == 0)
			{
				pItBegin = newLeaf;
				pItEnd = newLeaf;
				return;
			}
			pItBegin->pprev = newLeaf;
			pItBegin = newLeaf;
		}

		T 
		popFront ()
		{
			if (pItBegin == 0)
			{
				MyException("The list is empty\n");
			}
			T value = pItBegin->data;
			leaf* pdelete = pItBegin;
			pItBegin = pItBegin->pnext;
			pItBegin->pprev = 0;
			if (pItEnd == pdelete)
			{
				pItEnd = 0;
			}
			delete pdelete;
			return value;
		}

		// изменяет состояние итератора. выставляет предыдущую позицию.
		void 
		erase (CIterator& it)
		{
			if (!it.isValid())
			{
				MyException("Iterator is invalid\n");
			}
			leaf* pdelete = it.getLeaf();
			if (pdelete == 0)
			{
				MyException("Iterator is out of list\n");
			}
			if (pdelete == pItBegin)
			{
				pItBegin = pItBegin->pnext;
				pItBegin->pprev = 0;
				delete pdelete;
				if (pItBegin == 0)
				{
					pItEnd = 0;
				}
				it.setLeafPreBegin(pItBegin);
			}
			else
			{
				leaf* pcurrent = pItBegin->pnext;
				while (pcurrent != pdelete && pcurrent != 0)
				{
					pcurrent = pcurrent->pnext;
				}
				if (pcurrent == 0)
				{
					MyException("Iterator is out of the list\n");
				}
				pcurrent->pprev->pnext = pcurrent->pnext;
				if (pcurrent->pnext != 0)
				{
					pcurrent->pnext->pprev = pcurrent->pprev;
				}
				if (pcurrent == pItEnd)
				{
					pItEnd = pcurrent->pprev;
				}
				pcurrent = pcurrent->pprev;
				delete pdelete;
				it.setLeaf(pcurrent);
			}
		}

		// изменяет состояние итератора. выставляет следующую позицию.
		void 
		eraseAndNext (CIterator& it)
		{
			if (!it.isValid())
			{
				MyException("Iterator is invalid\n");
			}
			leaf* pdelete = it.getLeaf();
			if (pdelete == pItEnd)
			{
				pItEnd = pItEnd->pprev;
				pItEnd->pnext = 0;
				delete pdelete;
				if (pItEnd == 0)
				{
					pItBegin = 0;
				}
				it.setLeafPostEnd(pItEnd);
			}
			else
			{
				leaf* pcurrent = pItBegin;
				while (pcurrent != pdelete && pcurrent != 0)
				{
					pcurrent = pcurrent->pnext;
				}
				if (pcurrent == 0)
				{
					MyException("Iterator is out of the list\n");
				}
				pcurrent->pnext->pprev = pcurrent->pprev;
				if (pcurrent->pprev != 0)
				{
					pcurrent->pprev->pnext = pcurrent->pnext;
				}
				if (pcurrent == pItBegin)
				{
					pItBegin = pcurrent->pnext;
				}
				pcurrent = pcurrent->pnext;
				delete pdelete;
				it.setLeaf(pcurrent);
			}
		}

		//вставляет элемент на позицию, следующую за итератором. Итератор остается на месте
		void 
		insert (CIterator& it, T& data)
		{
			if (!it.isValid())
			{
				MyException("Iterator is invalid\n");
			}
			leaf* pinsert = it.getLeaf();
			if (pinsert == 0)
			{
				if (it.isPreBegin())
				{
					leaf* newLeaf = new leaf(data, 0, pItBegin);
					pItBegin->pprev = newLeaf;
					pItBegin = newLeaf;
					it.setLeafPreBegin(newLeaf);
					return;
				}
				else
				{
					//если попали в эту ветку, значит стоим за конечным элементом
					MyException("Iterator is out of the list\n");
				}
			}

			leaf* pcurrent = pItBegin;
			while (pcurrent != pinsert && pcurrent != 0)
			{
				pcurrent = pcurrent->pnext;
			}
			if (pcurrent == 0)
			{
				MyException("Iterator is out of the list\n");
			}
			if (pcurrent == pItEnd)
			{
				leaf* newLeaf = new leaf(data, pItEnd, 0);
				pItEnd->pnext = newLeaf;
				pItEnd = newLeaf;
				return;
			}
			leaf* newLeaf = new leaf(data, pcurrent, pcurrent->pnext);
			pcurrent->pnext->pprev = newLeaf;
			pcurrent->pnext = newLeaf;
		}

		int 
		getSize ()
		{
			int size = 0;
			leaf* pcurrent = pItBegin;
			while (pcurrent != 0)
			{
				size++;
				pcurrent = pcurrent->pnext;
			}
			return size;
		}

		void
		clear ()
		{
			leaf* pcurrent = pItBegin;
			while (pcurrent != 0)
			{
				pItBegin = pcurrent->pnext;
				delete pcurrent;
				pcurrent = pItBegin;
			}
			pItBegin = 0;
			pItEnd = 0;
		}

		CIterator begin ()
		{
			return CIterator(pItBegin);
		}

		CIterator end ()
		{
			CIterator it;
			it.setLeafPostEnd(pItEnd);
			return it;
		}
	};
}

#endif  //DOUBLYLINKEDLIST_H_2016_04_12
