#include "HelloWorldScene.h"
#include "MenuScene.h"
#include "SimpleAudioEngine.h"
#include "support/CCPointExtension.h"

using namespace cocos2d;
using namespace CocosDenshion;



CCScene* HelloWorld::scene()
{
    // 'scene' is an autorelease object
    CCScene *scene = CCScene::create();
    
    // 'layer' is an autorelease object
    HelloWorld *layer = HelloWorld::create();

    // add layer as a child to scene
    scene->addChild(layer);

    // return the scene
    return scene;
}

// on "init" you need to initialize your instance
bool HelloWorld::init()
{
    if ( !CCLayer::init() )
    {
        return false;
    }

    // Get the window size
    CCSize winSize = CCDirector::sharedDirector()->getWinSize();

    // Turn on touch
    this->setTouchEnabled(true);

    // Sound on?
    soundOn = CCUserDefault::sharedUserDefault()->getBoolForKey("soundOn");
    score = 0;

    // Set initial paddle velocity
    paddleVelocity = b2Vec2(5, 0);

    // Score label
	char scoreString[100];
	sprintf(scoreString, "Score: %d", score);
    scoreLabel = CCLabelTTF::labelWithString(scoreString, "Courier", 40.0);
    scoreLabel->setPosition(ccp(winSize.width / 2, winSize.height - 20));
    this->addChild(scoreLabel);

    desiredBallsize = winSize.width / 40;
    desiredPaddleWidth = winSize.width / 6; // This defines how wide to make the paddle
    desiredPaddleHeight= winSize.height / 20; // This defines how tall to make the paddle

    // Create sprite and add it to the layer
    _ball = CCSprite::create("ball.png");
    _ball->setScaleX(desiredBallsize / _ball->getTextureRect().size.width);
    _ball->setScaleY(desiredBallsize / _ball->getTextureRect().size.height);

    // Add ball
    _ball->setPosition(ccp(winSize.width / 2, winSize.height / 3));
    _ball->setTag(1);
    this->addChild(_ball);

    // Create a world for all physics
    b2Vec2 gravity = b2Vec2(0.0f, 0.0f);
    _world = new b2World(gravity);

    // Create walls of the world
	b2BodyDef groundBodyDef;
	groundBodyDef.position.Set(0,0);

	_groundBody = _world->CreateBody(&groundBodyDef);
	b2EdgeShape groundEdge;
	b2FixtureDef boxShapeDef;
	boxShapeDef.shape = &groundEdge;

	// Define the walls
	groundEdge.Set(b2Vec2(0,0), b2Vec2(winSize.width/PTM_RATIO, 0));
	_bottomFixture = _groundBody->CreateFixture(&boxShapeDef);

	groundEdge.Set(b2Vec2(0,0), b2Vec2(0,winSize.height/PTM_RATIO));
	_groundBody->CreateFixture(&boxShapeDef);

	groundEdge.Set(b2Vec2(0, winSize.height/PTM_RATIO),
				   b2Vec2(winSize.width/PTM_RATIO, winSize.height/PTM_RATIO));
	_groundBody->CreateFixture(&boxShapeDef);

	groundEdge.Set(b2Vec2(winSize.width/PTM_RATIO, winSize.height/PTM_RATIO),
				   b2Vec2(winSize.width/PTM_RATIO, 0));
	_groundBody->CreateFixture(&boxShapeDef);

    // Create ball physics
    b2BodyDef ballBodyDef;
    ballBodyDef.type = b2_dynamicBody;
    ballBodyDef.position.Set(0,0);
    ballBodyDef.position.Set( (winSize.width / 2 / PTM_RATIO), (winSize.height / 3 / PTM_RATIO));
    ballBodyDef.userData = _ball;
    _ballBody = _world->CreateBody(&ballBodyDef);

    b2CircleShape circle;
    circle.m_radius = desiredBallsize / 2 / PTM_RATIO;

    b2FixtureDef ballShapeDef;
    ballShapeDef.shape = &circle;
    ballShapeDef.density = 1.0f;
    ballShapeDef.friction = 0.0f;
    ballShapeDef.restitution = 1.0f;
    _ballFixture = _ballBody->CreateFixture(&ballShapeDef);

    b2Vec2 force = b2Vec2(2, 4);
    _ballBody->ApplyLinearImpulse(force, ballBodyDef.position);

    // Create paddle
    paddle = CCSprite::create("playerbar.png");
    paddle->setScaleX(desiredPaddleWidth / paddle->getTextureRect().size.width);
    paddle->setScaleY(desiredPaddleHeight / paddle->getTextureRect().size.height);
    paddle->setPosition(ccp(winSize.width / 2, 60));
    paddle->setTag(5);
    this->addChild(paddle);

    // Create paddle physics
    b2BodyDef paddleBodyDef;
    paddleBodyDef.type = b2_dynamicBody;
    paddleBodyDef.position.Set(winSize.width / 2 / PTM_RATIO, 60 / PTM_RATIO);
    paddleBodyDef.userData = paddle;
    _paddleBody = _world->CreateBody(&paddleBodyDef);

    b2PolygonShape paddleShape;
    paddleShape.SetAsBox(desiredPaddleWidth/PTM_RATIO/2,desiredPaddleHeight/PTM_RATIO/2);

    b2FixtureDef paddleShapeDef;
    paddleShapeDef.shape = &paddleShape;
    paddleShapeDef.density = 10.0f;
    paddleShapeDef.friction = 0.0f;
    paddleShapeDef.restitution = 0.0f;
    _paddleFixture = _paddleBody->CreateFixture(&paddleShapeDef);

    // Restrict paddle movement via a joint
    b2PrismaticJointDef jointDef;
    b2Vec2 worldAxis(1.0f, 0.0f);
    jointDef.collideConnected = true;
    jointDef.Initialize(_paddleBody, _groundBody,
      _paddleBody->GetWorldCenter(), worldAxis);
    _world->CreateJoint(&jointDef);

    // Set the contact listener for box
	_contactListener = new MyContactListener();
	_world->SetContactListener(_contactListener);

    // Draw the bricks
    drawBricks(3, 10);

    // Start the physics/game loop
    schedule(schedule_selector(HelloWorld::tick));

    return true;
}


void HelloWorld::gameOver(bool won) {
	CCSize winSize = CCDirector::sharedDirector()->getWinSize();

	if (won) {
		CCLabelTTF* wonLabel = CCLabelTTF::labelWithString("YOU WON!", "Courier", 60.0);
		wonLabel->setPosition(ccp(winSize.width / 2, winSize.height / 4 * 3));
		this->addChild(wonLabel);
	}
	else {
		CCLabelTTF* lostLabel = CCLabelTTF::labelWithString("YOU LOST! :(", "Courier", 60.0);
		lostLabel->setPosition(ccp(winSize.width / 2, winSize.height / 4 * 3));
		this->addChild(lostLabel);
	}

	unschedule(schedule_selector(HelloWorld::tick));

	CCMenuItemFont::setFontName("Courier");

	// Create "play," "scores," and "controls" buttons - when tapped, they call methods we define: playButtonAction and scoresButtonAction
	CCMenuItemFont *restartButton = CCMenuItemFont::create("Restart Game", this, menu_selector(HelloWorld::restartButtonAction));
	CCMenuItemFont *menuButton = CCMenuItemFont::create("Return to Menu", this, menu_selector(HelloWorld::menuButtonAction));

	// Create menu that contains our buttons
	CCMenu *menu = CCMenu::menuWithItems(restartButton, menuButton, NULL);

	// Align buttons horizontally
	menu->alignItemsHorizontallyWithPadding(40);

	// Set position of menu to be below the title text
	menu->setPosition(ccp(winSize.width / 2, winSize.height / 2));

	// Add menu to layer
	this->addChild(menu);


}

void HelloWorld::restartButtonAction(CCObject* pSender)
{
    CCDirector::sharedDirector()->replaceScene(HelloWorld::scene());
}

void HelloWorld::menuButtonAction(CCObject* pSender)
{
    CCDirector::sharedDirector()->replaceScene(Menu::scene());
}

void HelloWorld::increaseScore()
{
	score++;
	char scoreString[100];
	sprintf(scoreString, "Score: %d", score);
	scoreLabel->setString(scoreString);
}

void HelloWorld::tick (float32 dt)
{

	bool bricksLeft;

	_world->Step(dt, 10, 10);
	    for(b2Body *b = _world->GetBodyList(); b; b=b->GetNext()) {
	        if (b->GetUserData() != NULL) {
	            CCSprite *sprite = (CCSprite *)b->GetUserData();
	            sprite->setPosition(ccp(b->GetPosition().x * PTM_RATIO, b->GetPosition().y * PTM_RATIO));
	            sprite->setRotation(-1 * CC_RADIANS_TO_DEGREES(b->GetAngle()));

	            // Slow ball if its too fast
	            if (sprite->getTag() == 1) {

					b2Vec2 velocity = b->GetLinearVelocity();
					float32 speed = velocity.Length();

					if (speed > MAX_BALL_VELOCITY) {
						b->SetLinearDamping(0.3);
					} else if (speed < MAX_BALL_VELOCITY) {
						b->SetLinearDamping(0.0);
					}
				}
	            else if (sprite->getTag() == 2) {
	            	bricksLeft = true;
	            }
	        }
	    }

	    // Things that need to be removed from the scene
	    std::vector<b2Body *>toDestroy;
	    // Things that have collided
		std::vector<MyContact>::iterator pos;

		for(pos = _contactListener->_contacts.begin();pos != _contactListener->_contacts.end(); ++pos) {
			MyContact contact = *pos;

			if ((contact.fixtureA == _bottomFixture && contact.fixtureB == _ballFixture) ||
				(contact.fixtureA == _ballFixture && contact.fixtureB == _bottomFixture)) {
				gameOver(false);
			}

			b2Body *bodyA = contact.fixtureA->GetBody();
			b2Body *bodyB = contact.fixtureB->GetBody();
			if (bodyA->GetUserData() != NULL && bodyB->GetUserData() != NULL) {
				CCSprite *spriteA = (CCSprite *) bodyA->GetUserData();
				CCSprite *spriteB = (CCSprite *) bodyB->GetUserData();

				if (spriteA->getTag() == 1 && spriteB->getTag() == 2) {
					if (std::find(toDestroy.begin(), toDestroy.end(), bodyB)
						== toDestroy.end()) {
						toDestroy.push_back(bodyB);
						increaseScore();
					}
				} else if (spriteA->getTag() == 2 && spriteB->getTag() == 1) {
					if (std::find(toDestroy.begin(), toDestroy.end(), bodyA)
						== toDestroy.end()) {
						toDestroy.push_back(bodyA);
						increaseScore();
					}
				} else if ((spriteA->getTag() == 1 && spriteB->getTag() == 5) || (spriteA->getTag() == 5 && spriteB->getTag() == 1)) {
					// If its the ball and paddle, just play a sound
					if (soundOn) {
						SimpleAudioEngine::sharedEngine()->playEffect("boing.wav");
					}
				}
			}
		}

		std::vector<b2Body *>::iterator pos2;
		for(pos2 = toDestroy.begin(); pos2 != toDestroy.end(); ++pos2) {
			b2Body *body = *pos2;
			if (body->GetUserData() != NULL) {
				CCSprite *sprite = (CCSprite *) body->GetUserData();
				this->removeChild(sprite, true);
			}
			_world->DestroyBody(body);
		}

		if (!bricksLeft)
		{
			gameOver(true);
		}

		if ((toDestroy.size() > 0) && (soundOn == true))
		{
			SimpleAudioEngine::sharedEngine()->playEffect("pop.wav");
		}

}

void HelloWorld::dealloc () {
    delete _world;
    _ballBody = NULL;
    _world = NULL;
}


void HelloWorld::menuCloseCallback(CCObject* pSender)
{
    CCDirector::sharedDirector()->end();

#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
    exit(0);
#endif
}

void HelloWorld::registerWithTouchDispatcher(void)
{
    CCDirector* pDirector = CCDirector::sharedDirector();
    pDirector->getTouchDispatcher()->addTargetedDelegate(this, kCCMenuHandlerPriority + 1, true);

}

bool HelloWorld::ccTouchBegan(CCTouch* touch, CCEvent* event)
{
	CCSize winSize = CCDirector::sharedDirector()->getWinSize();
	float distance;
	float duration;

	CCPoint touchLocation = touch->locationInView(  );
	touchLocation = CCDirector::sharedDirector()->convertToGL(touchLocation);
	touchLocation =  convertToNodeSpace(touchLocation);

	    float screenCenter = winSize.width / 2;

	    if (touchLocation.x < screenCenter) {
	    	_paddleBody->SetLinearVelocity(b2Vec2(-PADDLE_VELOCITY,0));

	    }
	    else {
	    	_paddleBody->SetLinearVelocity(b2Vec2(PADDLE_VELOCITY,0));
	    }
	        _paddleBody->SetAwake(true);

    return true;
}

void HelloWorld::drawBricks(int numrows, int numbricks) {
	CCSize winSize = CCDirector::sharedDirector()->getWinSize();

	float margin = 6;
	float heightMargin = 6;

	// Divide the width of the screen into bricks
	desiredBrickWidth = (winSize.width - (margin * numbricks)) / numbricks;
	// Row height should always be 1/20 of the screen
	desiredBrickHeight = (winSize.height - (heightMargin * numrows)) / 20;
	// Needs to be the center of the width of the brick
	float baseBrickPositionX = desiredBrickWidth / 2;
	// Need to be the center of the height of the brick
	float baseBrickPositionY = winSize.height - winSize.height / 5;

	for (int rowcount = 0;rowcount < numrows;rowcount++) {

		for (int brickcount = 0;brickcount < numbricks;brickcount++) {

			float xPosition = (brickcount * (desiredBrickWidth + margin)) + baseBrickPositionX;
			float yPosition = baseBrickPositionY - (rowcount * (desiredBrickHeight + heightMargin));
			// Create the brick from the sprite file
			CCSprite* tempBrick = CCSprite::create("brick.png");
			// Set the position based on the number of bricks
			tempBrick->setPosition(ccp(xPosition, yPosition));
			// Set the scaling accordingly to make the brick meet the desired size on the screen
			tempBrick->setScaleX(desiredBrickWidth / tempBrick->getTextureRect().size.width);
			tempBrick->setScaleY(desiredBrickHeight / tempBrick->getTextureRect().size.height);
			// Add it to the layer
			tempBrick->setTag(2);
			this->addChild(tempBrick);

			// Create block body
			b2BodyDef brickBodyDef;
			brickBodyDef.type = b2_dynamicBody;
			brickBodyDef.position.Set(xPosition/PTM_RATIO, yPosition/PTM_RATIO);
			brickBodyDef.userData = tempBrick;
			b2Body *brickBody = _world->CreateBody(&brickBodyDef);

			// Create block shape
			b2PolygonShape brickShape;
			brickShape.SetAsBox(desiredBrickWidth/PTM_RATIO/2, desiredBrickHeight/PTM_RATIO/2);

			// Create shape definition and add to body
			b2FixtureDef brickShapeDef;
			brickShapeDef.shape = &brickShape;
			brickShapeDef.density = 10.0;
			brickShapeDef.friction = 0.0;
			brickShapeDef.restitution = 0.1f;
			brickBody->CreateFixture(&brickShapeDef);

		}
	}
}

/////////////////////////////////////////////////////////
// Touch on left side of screen moves player left
// Touch on right side of screen moves player right
void HelloWorld::ccTouchEnded(CCTouch* touch, CCEvent* event)
{
	// Just stop the movement in box
	_paddleBody->SetAwake(false);

}
