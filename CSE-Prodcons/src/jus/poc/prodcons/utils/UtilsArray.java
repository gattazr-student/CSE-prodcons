package jus.poc.prodcons.utils;

import java.lang.reflect.Array;

/**
 * @author gattazr
 * 
 */
public class UtilsArray {
	/**
	 * @param aObjects
	 *            an array of objects
	 * @return the common class of the objects stored in the array
	 */
	static Class<?> calcClassOfArrayElmts(Object[] aObjects) {
		return calcClassOfArrayElmts(aObjects, null);
	}

	/**
	 * @param aObjects
	 *            an array of objects
	 * @param aObjectToAdd
	 *            an object to add in the array
	 * @return the common class of the objects stored in the array and that of
	 *         the object to be added
	 */
	static Class<?> calcClassOfArrayElmts(Object[] aObjects,
			Object aObjectToAdd) {
		if (aObjects == null)
			return Object.class;
		int wMax = aObjects.length;
		if (wMax < 1)
			return Object.class;
		if (aObjects[0] == null)
			return Object.class;

		// if the class of all the objets stored ine the array is the same,
		// it's the found class.
		Class<?> wFoundClass = aObjects[0].getClass();
		for (int wI = 1; wI < wMax; wI++) {
			if (aObjects[wI] == null
					|| aObjects[wI].getClass() != wFoundClass) {
				wFoundClass = Object.class;
				// break
				wI = wMax;
			}
		}
		// if the object to add isn't null and if the found class does'nt
		// equal the class of the object => Object class
		if (aObjectToAdd != null && wFoundClass != aObjectToAdd.getClass())
			wFoundClass = Object.class;

		return wFoundClass;
	}

	/**
	 * @param aObjects
	 *            the original array of objects
	 * @param aIdx
	 *            the index of the object to remove
	 * @return the new array of objects
	 * @throws IndexOutOfBoundsException
	 */
	static Object[] removeOneObject(Object[] aObjects, int aIdx)
			throws IndexOutOfBoundsException {
		if (aObjects == null)
			return aObjects;
		int wLen = aObjects.length;
		if (wLen < 1)
			return aObjects;

		validObjectsIndex(aObjects, aIdx);

		int wNewLen = wLen - 1;
		Object[] wNewArray = (Object[]) Array
				.newInstance(calcClassOfArrayElmts(aObjects), wNewLen);

		// if we must remove the first object
		if (aIdx == 0)
			System.arraycopy(aObjects, 1, wNewArray, 0, wNewLen);
		// if we must remove the last object
		else if (aIdx == wLen - 1)
			System.arraycopy(aObjects, 0, wNewArray, 0, wNewLen);
		//
		else {
			// wLen = 10 and aIdx = 5 => wNewLen = 9
			// wSubLenA = aIdx = 5 (old index 0 to 4)
			// wSubLenb = wNewMax- aIdx = 4 (old index 6 to 9)
			System.arraycopy(aObjects, 0, wNewArray, 0, aIdx);
			System.arraycopy(aObjects, aIdx + 1, wNewArray, aIdx,
					wNewLen - aIdx);
		}

		return wNewArray;
	}

	/**
	 * @param aObjects
	 * @param aIdx
	 * @throws IndexOutOfBoundsException
	 */
	static void validObjectsIndex(Object[] aObjects, int aIdx)
			throws IndexOutOfBoundsException {
		if (aObjects == null)
			throw new IndexOutOfBoundsException("the target array is null");
		int wLen = aObjects.length;
		if (aIdx < 0 || aIdx > wLen - 1)
			throw new IndexOutOfBoundsException(
					String.format("index [%d] is less than zero", aIdx));
		if (aIdx > wLen - 1)
			throw new IndexOutOfBoundsException(String.format(
					"index [%d] is greater than len-1 (len=[%d])", aIdx, wLen));
	}
}
