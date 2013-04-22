#ifndef __OPTIONS_SCENE_H__
#define __OPTIONS_SCENE_H__

#include "cocos2d.h"

using namespace cocos2d;

class Options : public cocos2d::CCLayer
{

private:
	float temp;

public:
    virtual bool init();  

    static CCScene* scene();

    CREATE_FUNC(Options);

    // Relevant game functions
    void soundOnButtonAction(CCObject* pSender);
    void soundOffButtonAction(CCObject* pSender);
    void menuButtonAction(CCObject* pSender);

};

#endif  __OPTIONS_SCENE_H__
