/*
Copyright (c) 2011, Matt Joseph
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the <organization> nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.


University of Pittsburgh
CS449
Project 4
Modified: 2011-11-27 @ 00:45

NOTE:
This file is primarily licensed under the New BSD License, as described above.
It can be CONVERTED to a GPL license, if desired by the end user.

*/

#include <linux/fs.h>
#include <linux/init.h>
#include <linux/miscdevice.h>
#include <linux/module.h>
#include <asm/uaccess.h> // For copy_to_user
#include "pi.h" // For pi function

MODULE_LICENSE("Dual BSD/GPL");
MODULE_AUTHOR("encrypstream <mgj7@pitt.edu>");
MODULE_DESCRIPTION("Module that does digits of pi");
MODULE_VERSION("dev");

/* pi_read defines what happens when a program
 * tries to read from /dev/pi
 */
static ssize_t pi_read(struct file * file, char * buf, size_t count, loff_t *ppos)
{
	unsigned int allocate; // Used to work with the pi function.
	char *pi_buffer;
	
	allocate = (4 - (*ppos + count) % 4) + *ppos + count; // Round up to the next multiple of 4
	
	pi_buffer = (char*)kmalloc(allocate, GFP_KERNEL); // Allocate some extra space based on the rounded value
	
	/* This pi function assumes that you are giving it a buffer and the number of digits to calculate
	 * You can use ANY pi function with this, as it is used via an include up above.
	 * A version that does 4 digits at a time was used, hence the multiples of 4.
	 */
	pi(pi_buffer, allocate);
	
	/* Copies to the user space, but also verifies permissions and such, which is safer
	 * than simply writing directly to a user buffer. But this limits the char device to the
	 * amount of space available to kmalloc.
	 */
	if (copy_to_user(buf, pi_buffer + *ppos, count)) {
		kfree(pi_buffer);
		return -EINVAL;
	}
	
	kfree(pi_buffer); // Free the buffer
	*ppos += count; // Increment the position
	return count;
}

/* Set up what file functions can be performed
 * Only read is defined.
 */
static const struct file_operations pi_fops = {
	.owner		= THIS_MODULE,
	.read		= pi_read,
};

static struct miscdevice pi_driver = {
	// Assign random
	MISC_DYNAMIC_MINOR,
	// Call it pi
	"pi",
	// Define file operations for pi
	&pi_fops
};

static int __init pi_init(void)
{
	int ret;
	ret = misc_register(&pi_driver);
	if (ret) // If for some reason it couldn't register
		printk(KERN_ERR "Unable to register pi char device\n");

	return ret;
}

module_init(pi_init);

/* Sets up /sys/class/misc/pi
 * Must manually create /dev/pi
 */
static void __exit pi_exit(void)
{
	misc_deregister(&pi_driver);
}

module_exit(pi_exit);
