
#ifndef NENGO_GPU_JNI_H
#define NENGO_GPU_JNI_H

#ifdef __cplusplus
extern "C"{
#endif

#include <stdio.h>
#include <jni.h>

void getDecoders(JNIEnv* env, NengoGPUData* currentData, int* deviceForEnsemble, jobjectArray decoders_JAVA);
void createIndexArrayAndGatherData(int gatherData, float* data, float* tempData, int* indices, int* columnLengths, int* dataIndexor, int numRows, int numColumns );
void createHelper(int* helper, int* columnLengths, int numRows, int numColumns);

int* sort(int* values, int length, int order);

void storeTerminationData(JNIEnv* env, jobjectArray transforms_JAVA, jobjectArray tau_JAVA, jobjectArray isDecodedTermination_JAVA, NengoGPUData* currentData, int* networkArrayData);

void storeNeuronData(JNIEnv *env, jobjectArray neuronData_JAVA, NengoGPUData* currentData, int* deviceForEnsemble);

void storeEncoders(JNIEnv *env, jobjectArray encoders_JAVA, NengoGPUData currentData);

void storeDecoders(JNIEnv* env, jobjectArray decoders_JAVA, NengoGPUData* currentData, int* networkArrayData);

void assignNetworkArrayToDevice(int networkArrayIndex, int* networkArrayData, int* ensembleData, NengoGPUData* currentData);

void setupInput(int numProjections, projection* projections, NengoGPUData* currentData, int* networkArrayData);

void setupSpikes(int* collectSpikes, NengoGPUData* currentData);

#ifdef __cplusplus
}
#endif

#endif
