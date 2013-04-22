#include "OptionsScene.h"
#include "MenuScene.h"
#include "HelloWorldScene.h"
#include "SimpleAudioEngine.h"
#include "support/CCPointExtension.h"

using namespace cocos2d;
using namespace CocosDenshion;



CCScene* Options::scene()
{
    CCScene *scene = CCScene::create();
    
    Options *layer = Options::create();

    scene->addChild(layer);

    return scene;
}

bool Options::init()
{
    if (!CCLayer::init())
    {
        return false;
    }

    // Get the window size
    CCSize winSize = CCDirector::sharedDirector()->getWinSize();

    // Set the options screen title
	CCLabelTTF *title = CCLabelTTF::labelWithString("Options", "Courier", 64.0);

	// Set position to the top fourth
	title->setPosition(ccp(winSize.width / 2, winSize.height/4*3));

	// Add to layer
	this->addChild(title, 1);

	// Set the font as Courier
	CCMenuItemFont::setFontName("Courier");

	// Create sound on, sound off, and return to menu items
	CCMenuItemFont *soundOnButton = CCMenuItemFont::create("Turn Sound On", this, menu_selector(Options::soundOnButtonAction));
	CCMenuItemFont *soundOffButton = CCMenuItemFont::create("Turn Sound Off", this, menu_selector(Options::soundOffButtonAction));
	CCMenuItemFont *menuButton = CCMenuItemFont::create("Return to Menu", this, menu_selector(Options::menuButtonAction));

	// Create menu itself
	CCMenu *menu = CCMenu::menuWithItems(soundOnButton, soundOffButton, menuButton, NULL);

	// Horizontally aligned
	menu->alignItemsHorizontallyWithPadding(20);

	// Set menu position to center
	menu->setPosition(ccp(winSize.width / 2, winSize.height / 2));

	// Add menu to layer
	this->addChild(menu, 2);

    return true;
}

void Options::soundOnButtonAction(CCObject* pSender)
{
    CCUserDefault::sharedUserDefault()->setBoolForKey("soundOn", true);
}

void Options::soundOffButtonAction(CCObject* pSender)
{
	CCUserDefault::sharedUserDefault()->setBoolForKey("soundOn", false);
}

void Options::menuButtonAction(CCObject* pSender)
{
    CCDirector::sharedDirector()->replaceScene(Menu::scene());
}

