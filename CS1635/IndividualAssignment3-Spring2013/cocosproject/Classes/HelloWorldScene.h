#ifndef PTM_RATIO
#define PTM_RATIO 52.0
#endif

#ifndef PADDLE_VELOCITY
#define PADDLE_VELOCITY 13.0
#endif

#ifndef MAX_BALL_VELOCITY
#define MAX_BALL_VELOCITY 12.0
#endif

#ifndef __HELLOWORLD_SCENE_H__
#define __HELLOWORLD_SCENE_H__

#include "cocos2d.h"
#include "Box2D.h"
#include "MyContactListener.h"


using namespace cocos2d;

class HelloWorld : public cocos2d::CCLayer
{

private:
	float desiredBallsize;
	float desiredPaddleWidth;
	float desiredPaddleHeight;
	float desiredBrickWidth;
	float desiredBrickHeight;
	bool soundOn;
	int score;
	CCLabelTTF *scoreLabel;

	CCSpriteBatchNode * _batchNode;
	CCSprite * _paddle;
	CCSprite *player;
	CCSprite *paddle;
	CCArray *  _brickarray;
	CCSprite* temp;
	b2World *_world;
	b2Body *_groundBody;
	b2Fixture *_bottomFixture;
	b2Fixture *_ballFixture;
	b2Body *_ballBody;
	CCSprite *_ball;

	b2Body *_paddleBody;
	b2Fixture *_paddleFixture;

	b2Vec2 paddleVelocity;

	// For box
	MyContactListener *_contactListener;

public:
    virtual bool init();  

    static CCScene* scene();
    
    void menuCloseCallback(CCObject* pSender);

    CREATE_FUNC(HelloWorld);

    // Relevant game functions
    virtual void registerWithTouchDispatcher(void);
    virtual void ccTouchEnded(CCTouch* touch, CCEvent* event);
    bool ccTouchBegan(CCTouch* touch, CCEvent* event);
    void drawBricks(int numrows, int numbricks);
    void tick(float32 dt);
    void dealloc();
    void restartButtonAction(CCObject* pSender);
    void menuButtonAction(CCObject* pSender);
    void increaseScore();
    void gameOver(bool won);
};

#endif  __HELLOWORLD_SCENE_H__
