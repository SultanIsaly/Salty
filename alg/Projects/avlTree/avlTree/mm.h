#ifndef MEMORY_MANAGER_HEAD_H_2016_04_06
#define MEMORY_MANAGER_HEAD_H_2016_04_06

#define MAX_SIZE_AFTER_CLEAR 200

#include <new>
#include "Exception.h"

namespace good_morning
{

	template <class T>
	class CMemoryManager
	{
		struct block
		{
			T* pdata;
			block *pnext;
			int firstFreeIndex;
			int usedCount;
		};

		int m_blkSize;
		block* m_pBlocks;
		block *m_pCurrentBlk;
		bool m_isDeleteElementsOnDestruct;

	public:
		CMemoryManager(int defaultBlockSize, bool isDeleteElementsOnDestruct = false)
			: m_blkSize(defaultBlockSize)
			, m_isDeleteElementsOnDestruct(isDeleteElementsOnDestruct)
			, m_pBlocks(0)
			, m_pCurrentBlk(0)
		{}



		virtual ~CMemoryManager()
		{
			clear();
		}

		T*
			newObject()
		{
			if (m_pCurrentBlk == 0)
			{
				m_pBlocks = newBlock();
				m_pCurrentBlk = m_pBlocks;
			}

			bool currEmpty(m_pCurrentBlk->firstFreeIndex == -1);
			if (currEmpty)
			{
				block* pBlock = m_pBlocks;
				block* pprevBlock = 0;
				while (pBlock != 0 && pBlock->firstFreeIndex == -1)
				{
					pprevBlock = pBlock;
					pBlock = pBlock->pnext;
				}
				if (pBlock == 0)
				{
					m_pCurrentBlk = newBlock();
					pprevBlock->pnext = m_pCurrentBlk;
				}
				else
				{
					m_pCurrentBlk = pBlock;
				}
			}

			int freeIndex = m_pCurrentBlk->firstFreeIndex;
			m_pCurrentBlk->firstFreeIndex = *(reinterpret_cast<int*>((m_pCurrentBlk->pdata) + freeIndex));

			new (m_pCurrentBlk->pdata + freeIndex) T;
			m_pCurrentBlk->usedCount++;
			return m_pCurrentBlk->pdata + freeIndex;
		}



		bool
			deleteObject(T* p)
		{
			block* pBlock = m_pBlocks;
			bool compare(pBlock != 0);
			bool size(pBlock->pdata > p || pBlock->pdata + m_blkSize - 1 < p);
			while (compare && size)
			{
				pBlock = pBlock->pnext;
				size = (pBlock->pdata > p || pBlock->pdata + m_blkSize - 1 < p);
			}

			if (pBlock == 0)
			{
				return false;
			}

			int freeIndex = p - pBlock->pdata;
			p->~T();
			pBlock->usedCount--;
			*(reinterpret_cast<int*>(p)) = pBlock->firstFreeIndex;
			pBlock->firstFreeIndex = freeIndex;
			return true;
		}


		void
			clear()
		{
			if (m_pBlocks == 0)
			{
				return;
			}
			if (!m_isDeleteElementsOnDestruct)
			{
				block* pBlock = m_pBlocks;
				while (pBlock != 0)
				{
					if (pBlock->usedCount != 0)
					{
						throw CException();
					}
					pBlock = pBlock->pnext;
				}

				pBlock = m_pBlocks;
				block* pNext = 0;
				while (pBlock != 0)
				{
					pNext = pBlock->pnext;
					delete[] pBlock->pdata;
					delete pBlock;
					pBlock = pNext;
				}
			}
			else
			{
				block* pBlock = m_pBlocks;
				block* pnext = 0;
				bool* pBlockMask = new bool[m_blkSize];
				while (pBlock != 0)
				{
					pnext = pBlock->pnext;
					deleteBlock(pBlock, pBlockMask);
					pBlock = pnext;
				}
				delete[] pBlockMask;
			}
			m_pBlocks = 0;
			m_pCurrentBlk = 0;
		}


	private:
		block*
			newBlock()
		{
			block* pblk = new block;
			pblk->firstFreeIndex = 0;
			pblk->usedCount = 0;
			pblk->pnext = 0;
			pblk->pdata = reinterpret_cast<T*>(new char[m_blkSize * sizeof(T)]);
			T* pData = pblk->pdata;
			for (int i = 0; i < m_blkSize - 1; ++i)
			{
				*(reinterpret_cast<int*>(pData + i)) = i + 1;
			}
			*(reinterpret_cast<int*>(pData + m_blkSize - 1)) = -1;
			return pblk;
		}



		void
			deleteBlock(block *p, bool* pmask)
		{
			for (int i = 0; i < m_blkSize; ++i)
			{
				pmask[i] = true;
			}
			T* pData = p->pdata;
			int freeIndex = p->firstFreeIndex;
			while (freeIndex != -1)
			{
				pmask[freeIndex] = false;
				freeIndex = *(reinterpret_cast<int*>(pData + freeIndex));
			}
			for (int i = 0; i < m_blkSize; ++i)
			{
				if (pmask[i])
				{
					(pData + i)->~T();
				}
			}
			delete[] pData;
			delete p;
		}

	};

};

#endif // #define MEMORY_MANAGER_HEAD_H_2016_04_06