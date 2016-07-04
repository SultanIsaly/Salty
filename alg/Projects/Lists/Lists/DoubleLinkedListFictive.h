#ifndef DOUBLYLINKEDLISTFICTIVE_H_2016_04_12
#define DOUBLYLINKEDLISTFICTIVE_H_2016_04_12

namespace evilcookie
{

	template<class T>
	class CDoublyLinkedListFictive
	{

		leaf* m_pNil;

		struct leaf
		{
			T data;
			leaf* pnext, *pprev;

			leaf()
				: data(T())
				, pnext(0)
				, pprev(0)
			{
			}

			leaf(T& _data, leaf* _pprev, leaf* _pnext)
				: data(_data)
				, pprev(_pprev)
				, pnext(_pnext)
			{
			}
		};

	public:
		class CIterator
		{
		public:
			CIterator()
				: m_pBegin(0)
				, m_pCurrent(0)
				, m_pNil(0)
				, m_pEnd(0)
			{
			}

			CIterator(leaf *pNil, leaf *p)
				: m_pBegin(0)
				, m_pCurrent(p)
				, m_pNil(pNil)
				, m_pEnd(0)
			{
			}

			CIterator(const CIterator &src)
				: m_pBegin(src.m_pBegin)
				, m_pCurrent(src.m_pCurrent)
				, m_pNil(src.m_pNil)
				, m_pEnd(src.m_pEnd)
			{
			}

			~CIterator()
			{
			}

			CIterator& operator = (const CIterator& src)
			{
				m_pBegin = src.m_pBegin;
				m_pCurrent = src.m_pCurrent;
				m_pNil = src.m_pNil;
				m_pEnd = src.m_pEnd;
				return *this;
			}

			bool operator != (const CIterator&  it) const
			{
				return (m_pNil != it.m_pNil
					|| m_pCurrent != it.m_pCurrent
					|| (m_pBegin != 0 && m_pBegin != it.m_pBegin)
					|| (m_pEnd != 0 && m_pEnd != it.m_pEnd));
			}

			void operator++()
			{
				if (m_pBegin != 0)
				{
					m_pCurrent = m_pBegin;
					m_pBegin = 0;
					return;
				}
				if (m_pEnd != 0)
				{
					throw std::exception("Iterator is out of the list\n");
				}
				if (m_pCurrent->pnext == m_pNil)
				{
					m_pEnd = m_pCurrent;
					m_pCurrent = 0;
				}
				else
				{
					m_pCurrent = m_pCurrent->pnext;
				}
			}

			void operator--()
			{
				if (m_pBegin != 0)
				{
					throw std::exception("Iterator is out of the list\n");
				}
				if (m_pEnd != 0)
				{
					m_pCurrent = m_pEnd;
					m_pEnd = 0;
					return;
				}
				if (m_pCurrent->pprev == m_pNil)
				{
					m_pBegin = m_pCurrent;
					m_pCurrent = 0;
				}
				else
				{
					m_pCurrent = m_pCurrent->pprev;
				}
			}

			T& getData()
			{
				if (m_pBegin != 0 || m_pEnd != 0)
				{
					throw std::exception("Iterator is put of the list\n");
				}
				return m_pCurrent->data;
			}

			T& operator* ()
			{
				return getData();
			}

			leaf* getLeaf()
			{
				return m_pCurrent;
			}

			bool isPreBegin()
			{
				return (m_pBegin != 0);
			}

			void setLeaf(leaf* p)
			{
				m_pCurrent = p;
				m_pBegin = 0;
				m_pEnd = 0;
			}

			void setLeafPreBegin(leaf* p)
			{
				m_pCurrent = 0;
				m_pBegin = p;
				m_pEnd = 0;
			}

			void setLeafPostEnd(leaf* p)
			{
				m_pCurrent = 0;
				m_pBegin = 0;
				m_pEnd = p;
			}

			bool isValid()
			{
				return (m_pCurrent != 0 || m_pBegin != 0 || m_pEnd != 0);
			}

		private:
			//храним голову списка, если мы находимся перед началом
			leaf* m_pBegin;
			// храним текущее положение
			leaf* m_pCurrent;
			// храним фиктивный элемент
			leaf* m_pNil;
			//храним конец списка, если мы находимся после конца
			leaf* m_pEnd;
		};


	public:
		CDoublyLinkedListFictive()
		{
			m_pNil = new leaf();
			m_pNil->pnext = m_pNil;
			m_pNil->pprev = m_pNil;
		}

		virtual ~CDoublyLinkedListFictive()
		{
			clear();
			delete m_pNil;
		}

		void pushBack(T& data)
		{
			leaf* newEnd = new leaf(data, m_pNil->pprev, m_pNil);
			m_pNil->pprev->pnext = newEnd;
			m_pNil->pprev = newEnd;
		}

		T popBack()
		{
			if (m_pNil->pprev == m_pNil)
			{
				throw std::exception("The list is empty\n");
			}
			leaf* pdelete = m_pNil->pprev;
			T returnValue = pdelete->data;
			pdelete->pprev->pnext = m_pNil;
			m_pNil->pprev = pdelete->pprev;
			delete pdelete;
			return returnValue;
		}

		void pushFront(T& data)
		{
			leaf* newBegin = new leaf(data, m_pNil, m_pNil->pnext);
			m_pNil->pnext->pprev = newBegin;
			m_pNil->pnext = newBegin;
		}

		T popFront()
		{
			if (m_pNil->pnext == m_pNil)
			{
				throw std::exception("The list is empty\n");
			}
			leaf* pdelete = m_pNil->pnext;
			T returnValue = pdelete->data;
			pdelete->pnext->pprev = m_pNil;
			m_pNil->pnext = pdelete->pnext;
			delete pdelete;
			return returnValue;
		}

		// изменяет состояние итератора. выставляет предыдущую позицию.
		void erase(CIterator& it)
		{
			tryToFind(it);
			leaf* pdelete = it.getLeaf();
			leaf* pcurrent = pdelete->pprev;
			pcurrent->pnext = pdelete->pnext;
			pdelete->pnext->pprev = pcurrent;
			delete pdelete;
			if (pcurrent == m_pNil)
			{
				it.setLeafPreBegin(pcurrent->pnext);
			}
			else
			{
				it.setLeaf(pcurrent);
			}
		}

		// изменяет состояние итератора. выставляет следующую позицию.
		void eraseAndNext(CIterator& it)
		{
			tryToFind(it);
			leaf* pdelete = it.getLeaf();
			leaf* pcurrent = pdelete->pnext;
			pcurrent->pprev = pdelete->pprev;
			pdelete->pprev->pnext = pcurrent;
			delete pdelete;
			if (pcurrent == m_pNil)
			{
				it.setLeafPostEnd(pcurrent->pprev);
			}
			else
			{
				it.setLeaf(pcurrent);
			}
		}

		void insert(CIterator& it, T& data)
		{
			if (!it.isValid())
			{
				throw std::exception("Iterator is invalid\n");
			}
			leaf* pinsert = it.getLeaf();
			if (pinsert == 0)
			{
				if (it.isPreBegin())
				{
					pushFront(data);
					it.setLeafPreBegin(m_pNil->pnext);
					return;
				}
				else
				{
					throw std::exception("Iterator is out of the list\n");
				}
			}
			//В листе пусто?
			if (pinsert == m_pNil)
			{
				pushFront(data);
				it.setLeafPreBegin(m_pNil->pnext);
				return;
			}
			if (pinsert == m_pNil->pprev)
			{
				pushBack(data);
				return;
			}
			leaf* newLeaf = new leaf(data, pinsert, pinsert->pnext);
			pinsert->pnext->pprev = newLeaf;
			pinsert->pnext = newLeaf;
		}

		int getSize()
		{
			int size = 0;
			leaf* pcurrent = m_pNil->pnext;
			while (pcurrent != m_pNil)
			{
				++size;
				pcurrent = pcurrent->pnext;
			}
			return size;
		}

		void clear()
		{
			leaf* pcurrent = m_pNil->pnext;
			while (pcurrent != m_pNil)
			{
				pcurrent = pcurrent->pnext;
				delete pcurrent->pprev;
			}
			m_pNil->pnext = m_pNil;
			m_pNil->pprev = m_pNil;
		}

		CIterator begin()
		{
			return CIterator(m_pNil, m_pNil->pnext);
		}

		CIterator end()
		{
			CIterator it(m_pNil, 0);
			it.setLeafPostEnd(m_pNil->pprev);
			return it;
		}

		//change
		void tryToFind(CIterator& it)
		{
			if (!it.isValid())
			{
				throw std::exception("Iterator is invalid\n");
			}
			leaf* pfind = it.getLeaf();
			if (pfind == 0)
			{
				throw std::exception("Iterator is out of the list\n");
			}
			leaf* pcurrent = m_pNil->pnext;
			while (pcurrent != m_pNil && pcurrent != pfind)
			{
				pcurrent = pcurrent->pnext;
			}
			if (pcurrent == m_pNil)
			{
				throw std::exception("Iterator is out of list\n");
			}
		}
	};
};

#endif //#ifndef
