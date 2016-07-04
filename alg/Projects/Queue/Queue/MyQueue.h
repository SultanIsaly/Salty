#ifndef QUEUE_H_2016_04_12
#define QUEUE_H_2016_04_12

#include "Exception.h"

namespace magic_morning
{

	template<class T>
	class CQueue
	{

		struct leaf
		{
			T data;
			leaf* pNext;
			leaf(T& _data, leaf* _pnext = NULL)
				: data(_data)
				, pNext(_pnext)
			{}
		};

		leaf* m_pBegin;
		leaf* m_pEnd;

	public:
		CQueue()
			: m_pBegin(0)
			, m_pEnd(0)
		{}

		virtual ~CQueue()
		{
			clear();
		}

		void
			InQueue(T& data)
		{
			leaf* newLeaf = new leaf(data, 0);
			if (m_pBegin == 0)
			{
				m_pBegin = newLeaf;
				m_pEnd = newLeaf;
				return;
			}
			m_pEnd->pNext = newLeaf;
			m_pEnd = newLeaf;
		}

		T
			DeQueue()
		{
			if (m_pBegin == 0)
			{
				MyException("The queue is empty\n");
			}
			T Value = m_pBegin->data;
			leaf* pBegin = m_pBegin;
			if (m_pEnd == pBegin)
			{
				m_pEnd = 0;
			}
			m_pBegin = m_pBegin->pNext;
			delete pBegin;
			return Value;
		}

		bool
			isQueueEmpty()
		{
			if (m_pBegin == 0)
			{
				return true;
			}
			return false;
		}

		int
			getSize()
		{
			int size = 0;
			leaf* pIter = m_pBegin;
			while (pIter != 0)
			{
				++size;
				pIter = pIter->pNext;
			}
			return size;
		}

		void
			clear()
		{
			leaf* pIter = m_pBegin;
			while (pIter != 0)
			{
				pIter = pIter->pNext;
				delete m_pBegin;
				m_pBegin = pIter;
			}
			m_pBegin = 0;
			m_pEnd = 0;
		}

	};
}

#endif  //#ifndef QUEUE_H_2016_04_12