#ifndef DOUBLYLINKEDLIST_H_2016_04_12
#define DOUBLYLINKEDLIST_H_2016_04_12

#include "Exception.h"

namespace magic_morning
{
	template<class T>
	class CDoublyLinkedList
	{

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

		leaf* m_pBegin;
		leaf* m_pEnd;

	public:
		class CIterator
		{
			//храним голову списка, если мы находимся перед началом
			leaf* m_pBegin;
			// храним текущее положение
			leaf* m_pCurrent;
			//храним конец списка, если мы находимся после конца
			leaf* m_pEnd;

		public:
			CIterator()
				: m_pBegin(0)
				, m_pCurrent(0)
				, m_pEnd(0)
			{}

			CIterator(leaf *p)
				: m_pBegin(0)
				, m_pCurrent(p)
				, m_pEnd(0)
			{}

			CIterator(const CIterator &src)
				: m_pBegin(src.m_pBegin)
				, m_pCurrent(src.m_pCurrent)
				, m_pEnd(src.m_pEnd)
			{}

			~CIterator()
			{}

			CIterator&
				operator = (const CIterator&  src)
			{
				if (this == &src)
				{
					return *this;
				}
				m_pBegin = src.m_pBegin;
				m_pCurrent = src.m_pCurrent;
				m_pEnd = src.m_pEnd;
				return *this;
			}

			bool
			operator != (const CIterator&  it) const
			{
				bool equalityCurrentIterator(m_pCurrent != it.m_pCurrent);
				bool equalityBeginIterator(m_pBegin!= 0 &&m_pBegin != it.m_pBegin);
				bool equalityEndIterator(m_pEnd != 0 && m_pEnd != it.m_pEnd);
				return (equalityCurrentIterator || equalityBeginIterator || equalityEndIterator);
			}

			void
			operator++ ()
			{
				if (!isValid())
				{
					MyException("Iterator is invalid\n");
				}
				if (m_pBegin!= 0)
				{
					m_pCurrent =m_pBegin;
					m_pBegin= 0;
					return;
				}
				if (m_pEnd != 0)
				{
					MyException("Iterator is out of the list\n");
				}
				//если находимся в конце
				if (m_pCurrent->pnext == 0)
				{
					setLeafPostEnd(m_pCurrent);
					return;
				}
				m_pCurrent = m_pCurrent->pnext;
			}

			void
			operator--()
			{
				if (!isValid())
				{
					MyException("Iterator is invalid\n");
				}
				if (m_pEnd != 0)
				{
					m_pCurrent = m_pEnd;
					m_pEnd = 0;
					return;
				}
				if (m_pBegin!= 0)
				{
					MyException("Iterator is out of the list\n");
				}
				//если находимся в начале
				if (m_pCurrent->pprev == 0)
				{
					setLeafPreBegin(m_pCurrent);
					return;
				}
				m_pCurrent = m_pCurrent->pprev;
			}

			T&
			getData()
			{
				if (!isValid())
				{
					MyException("Iterator is invalid\n");
				}
				if (m_pCurrent == 0)
				{
					MyException("Iterator is out of the list\n");
				}
				return m_pCurrent->data;
			}

			T&
			operator* ()
			{
				return getData();
			}

			leaf*
			getLeaf()
			{
				return m_pCurrent;
			}

			void
			setLeaf(leaf* p)
			{
				m_pBegin= 0;
				m_pCurrent = p;
				m_pEnd = 0;
			}

			void
			setLeafPreBegin(leaf* p)
			{
				m_pBegin= p;
				m_pCurrent = 0;
				m_pEnd = 0;
			}

			void
			setLeafPostEnd(leaf* p)
			{
				m_pBegin= 0;
				m_pCurrent = 0;
				m_pEnd = p;
			}

			bool
			isValid()
			{
				return (m_pCurrent != 0 ||m_pBegin != 0 || m_pEnd != 0);
			}

			bool
			isPreBegin()
			{
				if (m_pBegin!= 0)
				{
					return true;
				}
				return false;
			}

		};


	public:

		CDoublyLinkedList()
			:m_pBegin(0)
			, m_pEnd(0)
		{};

		virtual ~CDoublyLinkedList()
		{
			clear();
		};

		void
		pushBack(T& data)
		{
			leaf* newLeaf = new leaf(data, m_pEnd, 0);
			if (m_pBegin== 0)
			{
				m_pBegin= newLeaf;
				m_pEnd = newLeaf;
				return;
			}
			m_pEnd->pnext = newLeaf;
			m_pEnd = newLeaf;
		}

		T
		popBack()
		{
			if (m_pBegin== 0)
			{
				MyException("The list is empty\n");
			}
			T value = m_pEnd->data;
			leaf* pdelete = m_pEnd;
			m_pEnd = m_pEnd->pprev;
			m_pEnd->pnext = 0;
			if (m_pBegin== pdelete)
			{
				m_pBegin= 0;
			}
			delete pdelete;
			return value;
		}

		void
		pushFront(T& data)
		{
			leaf* newLeaf = new leaf(data, 0,m_pBegin);
			if (m_pBegin== 0)
			{
				m_pBegin= newLeaf;
				m_pEnd = newLeaf;
				return;
			}
			m_pBegin->pprev = newLeaf;
			m_pBegin= newLeaf;
		}

		T
		popFront()
		{
			if (m_pBegin == 0)
			{
				MyException("The list is empty\n");
			}
			T value =m_pBegin->data;
			leaf* pdelete =m_pBegin;
			m_pBegin=m_pBegin->pnext;
			if (!m_pBegin == 0)
			{
				m_pBegin->pprev = 0;
			}
			if (m_pEnd == pdelete)
			{
				m_pEnd = 0;
			}
			delete pdelete;
			return value;
		}

		// изменяет состояние итератора. выставляет предыдущую позицию.
		void
		erase(CIterator& it)
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
			if (pdelete ==m_pBegin)
			{
				m_pBegin=m_pBegin->pnext;
				m_pBegin->pprev = 0;
				delete pdelete;
				if (m_pBegin== 0)
				{
					m_pEnd = 0;
				}
				it.setLeafPreBegin(m_pBegin);
			}
			else
			{
				leaf* pcurrent =m_pBegin->pnext;
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
				if (pcurrent == m_pEnd)
				{
					m_pEnd = pcurrent->pprev;
				}
				pcurrent = pcurrent->pprev;
				delete pdelete;
				it.setLeaf(pcurrent);
			}
		}

		// изменяет состояние итератора. выставляет следующую позицию.
		void
		eraseAndNext(CIterator& it)
		{
			if (!it.isValid())
			{
				MyException("Iterator is invalid\n");
			}
			leaf* pdelete = it.getLeaf();
			if (pdelete == m_pEnd)
			{
				m_pEnd = m_pEnd->pprev;
				m_pEnd->pnext = 0;
				delete pdelete;
				if (m_pEnd == 0)
				{
					m_pBegin= 0;
				}
				it.setLeafPostEnd(m_pEnd);
			}
			else
			{
				leaf* pcurrent =m_pBegin;
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
				if (pcurrent ==m_pBegin)
				{
					m_pBegin= pcurrent->pnext;
				}
				pcurrent = pcurrent->pnext;
				delete pdelete;
				it.setLeaf(pcurrent);
			}
		}

		//вставляет элемент на позицию, следующую за итератором. Итератор остается на месте
		void
		insert(CIterator& it, T& data)
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
					leaf* newLeaf = new leaf(data, 0,m_pBegin);
					m_pBegin->pprev = newLeaf;
					m_pBegin= newLeaf;
					it.setLeafPreBegin(newLeaf);
					return;
				}
				else
				{
					//если попали в эту ветку, значит стоим за конечным элементом
					MyException("Iterator is out of the list\n");
				}
			}

			leaf* pcurrent =m_pBegin;
			while (pcurrent != pinsert && pcurrent != 0)
			{
				pcurrent = pcurrent->pnext;
			}
			if (pcurrent == 0)
			{
				MyException("Iterator is out of the list\n");
			}
			if (pcurrent == m_pEnd)
			{
				leaf* newLeaf = new leaf(data, m_pEnd, 0);
				m_pEnd->pnext = newLeaf;
				m_pEnd = newLeaf;
				return;
			}
			leaf* newLeaf = new leaf(data, pcurrent, pcurrent->pnext);
			pcurrent->pnext->pprev = newLeaf;
			pcurrent->pnext = newLeaf;
		}

		int
		getSize()
		{
			int size = 0;
			leaf* pcurrent =m_pBegin;
			while (pcurrent != 0)
			{
				size++;
				pcurrent = pcurrent->pnext;
			}
			return size;
		}

		void
		clear()
		{
			leaf* pcurrent =m_pBegin;
			while (pcurrent != 0)
			{
				m_pBegin= pcurrent->pnext;
				delete pcurrent;
				pcurrent =m_pBegin;
			}
			m_pBegin= 0;
			m_pEnd = 0;
		}

		CIterator begin()
		{
			return CIterator(m_pBegin);
		}

		CIterator end()
		{
			CIterator it;
			it.setLeafPostEnd(m_pEnd);
			return it;
		}
	};
}

#endif  //DOUBLYLINKEDLIST_H_2016_04_12

