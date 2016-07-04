#ifndef QUEUE_H_2016_04_12
#define QUEUE_H_2016_04_12

#include "Exception.h"

namespace magic_morning
{

	template<class T>
	class CQueue
	{
		leaf* pBegin, *pEnd;

		struct leaf
		{
			T data;
			leaf* pNext;
			leaf(T& _data, leaf* _pnext = NULL)
				: data(_data)
				, pNext(_pnext)
			{
			}
		};

	public:

		CQueue ()
			: pBegin(0)
			, pEnd(0)
		{}

		virtual ~CQueue ()
		{
			clear();
		}

		void 
		InQueue (T& data)
		{
			leaf* newLeaf = new leaf(data, 0);
			if (pBegin == 0)
			{
				pBegin = newLeaf;
				pEnd = newLeaf;
				return;
			}
			pEnd->pNext = newLeaf;
			pEnd = newLeaf;
		}

		T 
		DeQueue()
		{
			if (m_pBegin == 0)
			{
				MyException("The queue is empty\n");
			}
			T Value = pBegin->data;
			leaf* pBegin = pBegin;
			if (pEnd == pBegin)
			{
				pEnd = 0;
			}
			pBegin = pBegin->pNext;
			delete pBegin;
			return Value;
		}

		bool 
		isQueueEmpty()
		{
			if (pBegin == 0)
			{
				return true;
			}
			return false;
		}

		int getSize()
		{
			int size = 0;
			leaf* pIter = pBegin;
			while (pIter != 0)
			{
				++size;
				pIter = pIter->pNext;
			}
			return size;
		}

		void clear()
		{
			leaf* pIter = pBegin;
			while (pIter != 0)
			{
				pIter = pIter->pNext;
				delete pBegin;
				pBegin = pIter;
			}
			pBegin = 0;
			pEnd = 0;
		}

}

#endif  //#ifndef QUEUE_H_2016_04_12
