#include "MenuScene.h"
#include "HelloWorldScene.h"
#include "OptionsScene.h"
#include "SimpleAudioEngine.h"
#include "support/CCPointExtension.h"

using namespace cocos2d;
using namespace CocosDenshion;



CCScene* Menu::scene()
{
    // 'scene' is an autorelease object
    CCScene *scene = CCScene::create();
    
    // 'layer' is an autorelease object
    Menu *layer = Menu::create();

    // add layer as a child to scene
    scene->addChild(layer);

    // return the scene
    return scene;
}

// on "init" you need to initialize your instance
bool Menu::init()
{
    if (!CCLayer::init())
    {
        return false;
    }

    // Get the window size
    CCSize winSize = CCDirector::sharedDirector()->getWinSize();

    // Create the title of the game
	CCLabelTTF *title = CCLabelTTF::labelWithString("Awesome Breakout Game", "Courier", 64.0);

	// Position title at top fourth of the screen
	title->setPosition(ccp(winSize.width / 2, winSize.height/4*3));

	// Add to layer
	this->addChild(title, 1);

	// Create the label for the author
	CCLabelTTF *author = CCLabelTTF::labelWithString("by Matt Joseph", "Courier", 64.0);

	// Position author text at center of the screen
	author->setPosition(ccp(winSize.width / 2, winSize.height / 2));

	this->addChild(author, 2);

	// Set the font to Courier
	CCMenuItemFont::setFontName("Courier");

	// Create play and options menu items
	CCMenuItemFont *playButton = CCMenuItemFont::create("Play", this, menu_selector(Menu::playButtonAction));
	CCMenuItemFont *optionsButton = CCMenuItemFont::create("Options", this, menu_selector(Menu::optionsButtonAction));

	// Create the actual menu
	CCMenu *menu = CCMenu::menuWithItems(playButton, optionsButton, NULL);

	// Horizontal menu
	menu->alignItemsHorizontallyWithPadding(20);

	// Set the position to the bottom fourth
	menu->setPosition(ccp(winSize.width / 2, winSize.height / 4));

	// Add menu to layer
	this->addChild(menu, 3);

    return true;
}

void Menu::playButtonAction(CCObject* pSender)
{
    CCDirector::sharedDirector()->replaceScene(HelloWorld::scene());
}

void Menu::optionsButtonAction(CCObject* pSender)
{
    CCDirector::sharedDirector()->replaceScene(Options::scene());
}

