#ifndef SINGLELINKEDLIST_H_2016_04_12
#define SINGLELINKEDLIST_H_2016_04_12

#include "Exception.h"

namespace magic_morning
{
	template <class T>
	class CSingleLinkedList
	{

		struct leaf
		{
			T data;
			leaf * pNext;

			leaf(T& _data, leaf * _pnext = 0)
				: data(_data)
				, pNext(_pnext)
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
		public:
			CIterator()
				: m_pCurrent(0)
				, m_pBegin(0)
			{}

			CIterator(leaf *p)
				: m_pCurrent(p)
				, m_pBegin(0)
			{}

			CIterator(const CIterator &src)
				: m_pCurrent(src.m_pCurrent)
				, m_pBegin(src.m_pBegin)
			{}

			~CIterator()
			{}

			CIterator&
			operator= (const CIterator&  src)
			{
				//проверка на самоприсваивание 
				if (this == &src)
				{
					return *this;
				}
				m_pCurrent = src.m_pCurrent;
				m_pBegin = src.m_pBegin;
				return *this;
			}

			bool
			operator!= (const CIterator&  it) const
			{
				bool isHeadCompare(m_pCurrent != it.m_pCurrent);
				bool isCurrentPositionCompare(m_pBegin != 0 && m_pBegin != it.m_pBegin);
				return (isHeadCompare || isCurrentPositionCompare);
			}

			void
			operator++ ()
			{
				if (m_pBegin == 0 && m_pCurrent == 0)
				{
					MyException("Iterator is out of list\n");
				}
				if (m_pBegin != 0)
				{
					m_pCurrent = m_pBegin;
					m_pBegin = 0;
				}
				else
				{
					m_pCurrent = m_pCurrent->pNext;
				}
			}

			T&
			getData()
			{
				if (m_pCurrent == 0)
				{
					MyException("Iterator is out of list\n");
				}
				return m_pCurrent->data;
			}

			T&
			operator* ()
			{
				if (m_pCurrent == 0)
				{
					MyException("Iterator is out of list\n");
				}
				return m_pCurrent->data;
			}

			leaf*
			getLeaf()
			{
				return m_pCurrent;
			}

			void
			setLeaf(leaf* p)
			{
				if (p == m_pBegin || p == m_pCurrent)
				{
					return;
				}
				m_pCurrent = p;
				m_pBegin = 0;
			}

			void
			setLeafPreBegin(leaf* p)
			{
				if (p == m_pBegin)
				{
					return;
				}
				m_pCurrent = 0;
				if (m_pBegin == 0)
				{
					m_pBegin = p;
					return;
				}
				m_pBegin = p;
			}

			bool
			isValid()
			{
				if (m_pCurrent == 0 && m_pBegin == 0)
				{
					return false;
				}
				return true;
			}
		};

		CSingleLinkedList()
			: m_pBegin(0)
			, m_pEnd(0)
		{}

		virtual ~CSingleLinkedList()
		{
			clear();
		}

		void
		pushBack(T& data)
		{
			leaf* newEnd = new leaf(data);
			if (m_pBegin == 0)
			{
				m_pBegin = newEnd;
				m_pEnd = newEnd;
			}
			else
			{
				m_pEnd->pNext = newEnd;
				m_pEnd = newEnd;
			}
		}

		void
		pushFront(T& data)
		{
			leaf* newBegin = new leaf(data);
			if (m_pBegin == 0)
			{
				m_pBegin = newBegin;
				m_pEnd = newBegin;
			}
			else
			{
				newBegin->pNext = m_pBegin;
				m_pBegin = newBegin;
			}
		}

		T
		popFront()
		{
			if (m_pBegin == 0)
			{
				MyException("The list is empty\n");
			}
			T returnValue = m_pBegin->data;
			leaf* begin = m_pBegin;
			m_pBegin = m_pBegin->pNext;
			delete begin;
			if (m_pBegin == 0)
			{
				m_pEnd = 0;
			}
			return returnValue;
		}

		// изменяет состояние итератора. выставляет предыдущую позицию.
		void
		erase(CIterator& it)
		{
			if (!it.isValid())
			{
				throw new std::exception("Iterator is not valid\n");
			}
			leaf* pdelete = it.getLeaf();
			if (pdelete == m_pBegin)
			{
				m_pBegin = m_pBegin->pNext;
				delete pdelete;
				if (m_pBegin == 0)
				{
					m_pEnd = 0;
				}
				it.setLeafPreBegin(m_pBegin);
			}
			else
			{
				leaf* pcurrent = m_pBegin;
				while (pcurrent->pNext != pdelete && pcurrent != 0)
				{
					pcurrent = pcurrent->pNext;
				}
				if (pcurrent == 0)
				{
					MyException("Iterator is not valid\n");
				}
				pcurrent->pNext = pcurrent->pNext->pNext;
				delete pdelete;
				it.setLeaf(pcurrent);
			}
		}

		int
		getSize()
		{
			int size = 0;
			leaf* current = m_pBegin;
			while (current != 0)
			{
				current = current->pNext;
				++size;
			}
			return size;
		}

		void
		clear()
		{
			leaf* current = m_pBegin;
			while (current != 0)
			{
				m_pBegin = current->pNext;
				delete current;
				current = m_pBegin;
			}
			m_pBegin = 0;
			m_pEnd = 0;
		}

		CIterator
		begin()
		{
			return CIterator(m_pBegin);
		}

		CIterator
		end()
		{
			return CIterator(m_pEnd);
		}

	};
}
#endif //  ifndef SINGLELINKEDLIST_H_2016_04_11 
