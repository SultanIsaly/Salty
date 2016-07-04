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

			leaf (T& _data, leaf * _pnext = 0)
				: data(_data)
				, pNext(_pnext)
			{}
		};

		public:
			class CIterator
			{
				//храним голову списка, если мы находимся перед началом
				leaf* pBegin;
				// храним текущее положение
				leaf* pCurrent;
			public:
				CIterator ()
					: m_pCurrent(0)
					, m_pBegin(0)
				{}

				CIterator (leaf *p)
					: pCurrent(p)
					, pBegin(0)
				{}

				CIterator (const CIterator &src)
					: pCurrent(src.m_pCurrent)
					, pBegin(src.m_pBegin)
				{}

				~CIterator()
				{}

				CIterator& 
				operator= (const CIterator&  src)
				{
					//проверка на самоприсваивание 
					if (this == &src) {
						return *this;
					}
					pCurrent = src.pCurrent;
					pBegin = src.pBegin;
					return *this;
				}

				bool 
				operator!= (const CIterator&  it) const
				{
					bool isHeadCompare (pCurrent != it.pCurrent);
					bool isCurrentPositionCompare (pBegin != 0 && pBegin != it.pBegin)
					return (isHeadCompare || isCurrentPositionCompare);
				}

				void 
				operator++ ()
				{
					if (pBegin == 0 && pCurrent == 0)
					{
						MyException ("Iterator is out of list\n");
					}
					if (pBegin != 0)
					{
						pCurrent = pBegin;
						pBegin = 0;
					}
					else
					{
						pCurrent = pCurrent->pNext;
					}
				}

				T& 
				getData ()
				{
					if (pCurrent == 0)
					{
						MyException ("Iterator is out of list\n");
					}
					return pCurrent->data;
				}

				T& 
				operator* ()
				{
					if (pCurrent == 0)
					{
						MyException ("Iterator is out of list\n");
					}
					return pCurrent->data;
				}

				leaf* 
				getLeaf ()
				{
					return pCurrent;
				}

				void 
				setLeaf (leaf* p)
				{
					pCurrent = p;
					pBegin = 0;
				}

				void 
				setLeafPreBegin (leaf* p)
				{
					pCurrent = 0;
					pBegin = p;
				}

				bool isValid()     
				{
					if (pCurrent == 0 && pBegin == 0)
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

			virtual ~CSingleLinkedList()
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
					MyException ("The list is empty\n");
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
					//свой класс
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
						MyException ("Iterator is not valid\n");
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
				leaf* current = pBegin;
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
			begin()
			{
				return CIterator(pBegin);
			}

			CIterator 
			end()
			{
				return CIterator(pEnd);
			}

		};
}
#endif //  ifndef SINGLELINKEDLIST_H_2016_04_11