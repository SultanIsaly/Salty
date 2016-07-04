#ifndef SINGLELINKEDLIST_H_2016_04_12
#define SINGLELINKEDLIST_H_2016_04_12

#include "Exception.h"

namespace magic_morning
{
	template <class T>
	class CSingleLinkedList
	{
		leaf* pBegin, pEnd;

		struct leaf
		{
			T data;
			leaf * pNext;

			leaf(T& _data, leaf * _pnext = 0)
				: data(_data)
				, pNext(_pnext)
			{}
		};

	public:
		class CIterator
		{
			//храним голову списка, если мы находимся перед началом
			leaf* p_Begin;
			// храним текущее положение
			leaf* p_Current;
		public:
			CIterator ()
				: p_Current(0)
				, p_Begin(0)
			{}

			CIterator (leaf *p)
				: p_Current(p)
				, p_Begin(0)
			{}

			CIterator (const CIterator &src)
				: p_Current(src.p_Current)
				, p_Begin(src.p_Begin)
			{}

			~CIterator ()
			{}

			CIterator&
			operator= (const CIterator&  src)
			{
				//проверка на самоприсваивание 
				if (this == &src) 
				{
					return *this;
				}
				p_Current = src.p_Current;
				p_Begin = src.p_Begin;
				return *this;
			}

			bool
			operator!= (const CIterator&  it) const
			{
				bool isHeadCompare(p_Current != it.p_Current);
				bool isCurrentPositionCompare(p_Begin != 0 && p_Begin != it.p_Begin);
				return (isHeadCompare || isCurrentPositionCompare);
			}

			void
			operator++ ()
			{
				if (p_Begin == 0 && p_Current == 0)
				{
					MyException("Iterator is out of list\n");
				}
				if (p_Begin != 0)
				{
					p_Current = p_Begin;
					p_Begin = 0;
				}
				else
				{
					p_Current = p_Current->pNext;
				}
			}

			T&
			getData ()
			{
				if (p_Current == 0)
				{
					MyException("Iterator is out of list\n");
				}
				return p_Current->data;
			}

			T&
			operator* ()
			{
				if (p_Current == 0)
				{
					MyException("Iterator is out of list\n");
				}
				return p_Current->data;
			}

			leaf*
			getLeaf ()
			{
				return p_Current;
			}

			void
			setLeaf (leaf* p)
			{
				if (p == p_Begin || p == p_Current)
				{
					return;
				}
				//p->pNext = p_Current;
				p_Current = p;
				p_Begin = 0;
			}

			void
			setLeafPreBegin (leaf* p)
			{
				if (p == p_Begin)
				{
					return;
				}
				p_Current = 0;
				if (p_Begin == 0)
				{
					p_Begin = p;
					return;
				}
				//p->pNext = p_Begin;
				p_Begin = p;
			}

			bool 
			isValid ()
			{
				if (p_Current == 0 && p_Begin == 0)
				{
					return false;
				}
				return true;
			}
		};

		CSingleLinkedList ()
			: pBegin(0)
			, pEnd(0)
		{}

		virtual ~CSingleLinkedList ()
		{
			clear();
		}

		void
		pushBack (T& data)
		{
			leaf* newEnd = new leaf(data);
			if (pBegin == 0)
			{
				pBegin = newEnd;
				pEnd = newEnd;
			}
			else
			{
				pEnd->pNext = newEnd;
				pEnd = newEnd;
			}
		}

		void
		pushFront (T& data)
		{
			leaf* newBegin = new leaf(data);
			if (pBegin == 0)
			{
				pBegin = newBegin;
				pEnd = newBegin;
			}
			else
			{
				newBegin->pNext = pBegin;
				pBegin = newBegin;
			}
		}

		T
		popFront ()
		{
			if (m_pBegin == 0)
			{
				MyException("The list is empty\n");
			}
			T returnValue = pBegin->data;
			leaf* begin = pBegin;
			pBegin = pBegin->pNext;
			delete begin;
			if (pBegin == 0)
			{
				pEnd = 0;
			}
			return returnValue;
		}

		// изменяет состояние итератора. выставляет предыдущую позицию.
		void
		erase (CIterator& it)
		{
			if (!it.isValid())
			{
				throw new std::exception("Iterator is not valid\n");
			}
			leaf* pdelete = it.getLeaf();
			if (pdelete == pBegin)
			{
				pBegin = pBegin->pNext;
				delete pdelete;
				if (pBegin == 0)
				{
					pEnd = 0;
				}
				it.setLeafPreBegin(pBegin);
			}
			else
			{
				leaf* pcurrent = pBegin;
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
		getSize ()
		{
			int size = 0;
			leaf* current = pBegin;
			while (current != 0)
			{
				current = current->pNext;
				++size;
			}
			return size;
		}

		void
		clear ()
		{
			leaf* current = pBegin;
			while (current != 0)
			{
				pBegin = current->pNext;
				delete current;
				current = pBegin;
			}
			pBegin = 0;
			pEnd = 0;
		}

		CIterator
		begin ()
		{
			return CIterator(pBegin);
		}

		CIterator
		end ()
		{
			return CIterator(pEnd);
		}

	};
}
#endif //  ifndef SINGLELINKEDLIST_H_2016_04_11 
