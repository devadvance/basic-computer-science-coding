#ifndef __MENU_SCENE_H__
#define __MENU_SCENE_H__

#include "cocos2d.h"

using namespace cocos2d;

class Menu : public cocos2d::CCLayer
{

private:
	float temp;

public:
    virtual bool init();  
    static CCScene* scene();

    CREATE_FUNC(Menu);

    void playButtonAction(CCObject* pSender);
    void optionsButtonAction(CCObject* pSender);
};

#endif  __MENU_SCENE_H__
