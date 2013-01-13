/*
	***************************************************************
	For use with:
	FUSE: Filesystem in Userspace
	Copyright (C) 2001-2007  Miklos Szeredi <miklos@szeredi.hu>
	This program can be distributed under the terms of the GNU GPL.
	See the file COPYING.
	***************************************************************
	
	This version of cs1550.c:
	Copyright (c) 2012, Matt Joseph
	
	FUSE is licensed under the GPLv2. Thus, to maximize compatibility, and to avoid
	potential license/copyright issues, this program (cs1550.c) produced by me, Matt Joseph,
	using the basic SSO (Sequence, Structure, and Organization) skeleton code as provided
	to me, hereby license the resulting derivative work under the GPLv2. The file, COPYING,
	contains the full text of the GNU GPLv2
	
	This program can be distributed under the terms of the GNU GPL.
	See the file COPYING.
	
	University of Pittsburgh
	CS1550
	Project 3
	Modified: 2012-07-29 @ 22:50

	Note: Tested successfully by compiling with the following argument:
	gcc -Wall `pkg-config fuse --cflags --libs` cs1550.c -o cs1550
*/

#define	FUSE_USE_VERSION 26

#include <fuse.h>
#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <fcntl.h>

//size of a disk block
#define	BLOCK_SIZE 512

//we'll use 8.3 filenames
#define	MAX_FILENAME 8
#define	MAX_EXTENSION 3

//How many files can there be in one directory?
#define	MAX_FILES_IN_DIR (BLOCK_SIZE - (MAX_FILENAME + 1) - sizeof(int)) / \
	((MAX_FILENAME + 1) + (MAX_EXTENSION + 1) + sizeof(size_t) + sizeof(long))

//How much data can one block hold?
#define	MAX_DATA_IN_BLOCK (BLOCK_SIZE - sizeof(size_t) - sizeof(long))

// Number of blocks in this disk. Set to 5MB worth of blocks
#define DISK_SIZE 10240

// Number of blocks needed for the bitmap - NO BITMAP IMPLEMENTED!
#define BITMAP_BLOCKS (DISK_SIZE / BLOCK_SIZE)

struct cs1550_directory_entry
{
	char dname[MAX_FILENAME	+ 1];	//the directory name (plus space for a nul)
	int nFiles;			//How many files are in this directory. 
					//Needs to be less than MAX_FILES_IN_DIR

	struct cs1550_file_directory
	{
		char fname[MAX_FILENAME + 1];	//filename (plus space for nul)
		char fext[MAX_EXTENSION + 1];	//extension (plus space for nul)
		size_t fsize;			//file size
		long nStartBlock;		//where the first block is on disk
	} files[MAX_FILES_IN_DIR];		//There is an array of these
};

typedef struct cs1550_directory_entry cs1550_directory_entry;

struct cs1550_disk_block
{
	//Two choices for interpreting size: 
	//	1) how many bytes are being used in this block
	//	2) how many bytes are left in the file
	//Either way, size tells us if we need to chase the pointer to the next
	//disk block. Use it however you want.
	size_t size;	

	//The next disk block, if needed. This is the next pointer in the linked 
	//allocation list
	long nNextBlock;

	//And all the rest of the space in the block can be used for actual data
	//storage.
	char data[MAX_DATA_IN_BLOCK];
};

typedef struct cs1550_disk_block cs1550_disk_block;




/* CUSTOM MULTIUSE FUNCTIONS 
**********************************************************************************
**********************************************************************************
*/



/*
 * find_file
 * 
 * Returns the location (in .disk) of a file in a given directory
 * 
 * Returns:
 * position of the file on success
 * -1 on failure
 */
static int find_file(cs1550_directory_entry *working_directory, char *filename, char *extension) {
	int counter;
	for (counter = 0; counter < working_directory->nFiles; counter++) {
		if ((strcmp(filename, working_directory->files[counter].fname) == 0) && (strlen(filename) == strlen(working_directory->files[counter].fname))) { // If the filename is the same
			if ((extension[0] == '\0') && (working_directory->files[counter].fext[0] == '\0')) { // If the extensions are NULL
				return counter;
			}
			else if ((extension != NULL) && (strcmp(extension, working_directory->files[counter].fext) == 0)) { // If the extension isn't NULL and it matches the one in the file
				return counter;
			}
		}
	}
	
	return -1;
}


/*
 * update_directory
 * 
 * Update the directory in the .directories file. Used for mknod (and probably rmdir if that were implemented)
 * 
 * Returns:
 * 0 on success
 * -1 on failure
 */
static int update_directory(cs1550_directory_entry *directory_name, int position) {
	int return_value = -1;

	if (directory_name == NULL) { // If a bad directory was passed
		return_value = -1;
	}
	else {
		FILE *f = fopen(".directories", "r+");
		fseek(f, sizeof(cs1550_directory_entry) * position, SEEK_SET);
		fwrite(directory_name, sizeof(cs1550_directory_entry),1,f);
		fclose(f);
		return_value = 0;
	}

	return return_value;
}


/*
 * allocate_block
 * 
 * Used to allocate a new block (creating a file)
 * 
 * Returns:
 * position of the new block on success
 * -1 on failure
 */
static int allocate_block(void) {
	int block_position = -1;
	int new_block_position;
	int seek_return, read_return, write_return;
	FILE *f = fopen(".disk", "r+");
	if (f == NULL) // If .disk does not exist
		return -1;
	
	seek_return = fseek(f, -sizeof(cs1550_disk_block), SEEK_END);
	if (seek_return == -1) {
		return -1; // There was a problem seeking
	}
	
	read_return = fread(&block_position, sizeof(int), 1, f);
	if(read_return == -1) {
		return -1; // Problem reading
	}
	
	seek_return = fseek(f, -sizeof(cs1550_disk_block), SEEK_END);
	if (seek_return == -1) {
		return -1; // There was a problem seeking
	}
	
	new_block_position = block_position + 1;
	write_return = fwrite(&new_block_position, 1, sizeof(int), f);
	if (write_return == -1) {
		return -1; // There was a problem writing
	}
	
	fclose(f);
	return block_position;
}


/*
 * read_block
 * 
 * Used to read a block from disk and load it to memory
 * 
 * Requires:
 * A disk block pointer
 * Position of the block in the .disk file
 * 
 * Returns:
 * -1 on failure/error
 * 0 on success
 */
static int read_block(cs1550_disk_block *return_block, int position) {
	int return_value = -1;
	int seek_return, read_return;
	FILE *f = fopen(".disk", "r");
	if (f == NULL)
		return return_value;
	
	seek_return = fseek(f, sizeof(cs1550_disk_block) * position, SEEK_SET);
	if (seek_return == -1)
		return return_value;
	
	read_return = fread(return_block, sizeof(cs1550_disk_block), 1, f);
	if(read_return == 1){
		return_value = 0;
	}
	
	fclose(f);
	return return_value;
}


/*
 * write_block
 * 
 * Used to write a block to disk from memory
 * 
 * Requires:
 * A disk block pointer
 * Position of the block in the .disk file
 * 
 * Returns:
 * -1 on failure/error
 * 0 on success
 */
static int write_block(cs1550_disk_block *return_block, int position) {
	int return_value = -1;
	int seek_return, write_return;
	FILE *f = fopen(".disk", "r+");
	if (f == NULL)
		return return_value;
	
	seek_return = fseek(f, sizeof(cs1550_disk_block) * position, SEEK_SET);
	if (seek_return == -1)
		return return_value;
	
	write_return = fwrite(return_block, sizeof(cs1550_disk_block), 1, f);
	if(write_return == 1){
		return_value = 0;
	}
	
	fclose(f);
	return return_value;
}


/*
 * buffer_to_block
 * 
 * Transfers the data from a buffer to the data section of a block
 * 
 * Requires:
 * A disk block pointer
 * A buffer pointer
 * Position in the data section to start transfering to
 * Amount of data left to transfer
 * 
 * Returns:
 * Positive integer representing the amount of data left to transfer
 * 0 if there is no data left to transfer
 */
static int buffer_to_block(cs1550_disk_block *working_block, const char *buffer, int position, int data_left) {
	while(data_left > 0) {
		if(position > MAX_DATA_IN_BLOCK) { // If this block is not enough
			return data_left; // Return the amount of data left to write
		}
		else {
			
			working_block->data[position] = *buffer;
			buffer++; // Move the buffer pointer
			data_left--; // Decrement the amount of data left
			if(working_block->size <= position) { // If the size in the block is equal or less than the "new" size, update it
				working_block->size += 1;
			}
			
			position++;
		}
	}
	return data_left;
}


/*
 * block_to_buffer
 * 
 * Transfers the data from the data section of a block to a buffer
 * 
 * Requires:
 * A disk block pointer
 * A buffer pointer
 * Position in the data section to start transfering to
 * Amount of data left to transfer
 * 
 * Returns:
 * Positive integer representing the amount of data left to transfer
 * 0 if there is no data left to transfer
 * -1 if too much data was asked for
 */
static int block_to_buffer(cs1550_disk_block *working_block, char *buffer, int position, int data_left) {
	while(data_left > 0) {
		if(position > MAX_DATA_IN_BLOCK) { // If this block is below the position for reading
			return data_left; // Return the amount of data left to read
		}
		else {
			*buffer = working_block->data[position];
			buffer++; // Move the buffer pointer
			data_left--; // Decrement the amount of data left
			position++;
		}
	}
	return data_left;
}


/*
 * get_directory
 * 
 * Used to get the directory entry for an directory in a specific position in the .directories file
 * 
 * Requires:
 * A directory entry pointer
 * Position of the directory entry in the .directories file (can be found with find_directory)
 * 
 * Returns:
 * 0 on failure/error
 * 1 on success
 */
static int get_directory(cs1550_directory_entry *found_dir, int position) {
	int return_value = 0;
	int seek_return, read_return;
	FILE *f = fopen(".directories", "r");
	if (f == NULL) {
		return return_value;
	}
	seek_return = fseek(f, sizeof(cs1550_directory_entry) * position, SEEK_SET);
	if (seek_return == -1) {
		return return_value;
	}
	read_return = fread(found_dir, sizeof(cs1550_directory_entry), 1, f);
	if(read_return == 1) {
		return_value = 1;
	}
	fclose(f);
	return return_value;
}

/*
 * find_directory
 * 
 * Used to get the directory entry position for a specified directory
 * 
 * Requires:
 * A directory name
 * 
 * Returns:
 * -1 on failure/error
 * position in the .directories file of the correct entry on success
 */
// Searches through the .directories file and returns the position of that directory entry
static int find_directory(char *directory_name) {
	int return_value = -1; // Set to -1 by default
	cs1550_directory_entry temp_dir; // temp_dir
	int position = 0;
	while ((get_directory(&temp_dir, position) != 0) && (return_value == -1)) {
		if (strcmp(directory_name, temp_dir.dname) == 0) {
			return_value = position;
		}
		position++;
	}
	
	return return_value;
}


/*
**********************************************************************************
**********************************************************************************
*/


/*
 * Called whenever the system wants to know the file attributes, including
 * simply whether the file exists or not. 
 *
 * man -s 2 stat will show the fields of a stat structure
 */
static int cs1550_getattr(const char *path, struct stat *stbuf)
{
	cs1550_directory_entry current_dir;
	char directory[MAX_FILENAME + 1], filename[MAX_FILENAME + 1], extension[MAX_EXTENSION + 1];
	int return_value = -ENOENT;
	int dir_position, i;
	
	memset(stbuf, 0, sizeof(struct stat));
	
	// Just asking for root directory
	if (strcmp(path, "/") == 0) {
		stbuf->st_mode = S_IFDIR | 0755;
		stbuf->st_nlink = 2;
		return_value = 0; // no error
	}
	else { // Not just the root directory
		memset(directory, 0, MAX_FILENAME + 1);
		memset(filename, 0, MAX_FILENAME + 1);
		memset(extension, 0, MAX_EXTENSION + 1);
		
		sscanf(path, "/%[^/]/%[^.].%s", directory, filename, extension);
		dir_position = find_directory(directory); // Find directory position
		if (dir_position != -1) { // If it is a valid directory
			get_directory(&current_dir,dir_position);
			
			// Set for both files and directories
			stbuf->st_atime = 0; /* time of last access */
			stbuf->st_mtime = 0; /* time of last modification */
			stbuf->st_ctime = 0; /* time of last status change */
			stbuf->st_uid = 0; /* user ID of owner */
			stbuf->st_gid = 0; /* group ID of owner */
			
			if (directory != NULL && (filename[0] == '\0')) { // If it is a directory only
				stbuf->st_mode = S_IFDIR | 0755;
				stbuf->st_nlink = 2;
				return_value = 0; //no error
			}
			else { // If it is a file
				for (i = 0; i < current_dir.nFiles; i++) {
					if (strcmp(current_dir.files[i].fname,filename) == 0) { // If the filename matches
						// Assumes an file without an extension has a NULL extension
						if (strcmp(current_dir.files[i].fext,extension) == 0) { // If the extension matches
							stbuf->st_mode = S_IFREG | 0666; 
							stbuf->st_nlink = 1; // file links
							stbuf->st_size = current_dir.files[i].fsize; // file size
							return_value = 0; // no error
							break;
						}
					}
				}
			}
		}
		else { // Invalid directory
			return_value = -ENOENT; // directory does not exists
		}
	}
	return return_value;
}

/* 
 * Called whenever the contents of a directory are desired. Could be from an 'ls'
 * or could even be when a user hits TAB to do autocompletion
 */
static int cs1550_readdir(const char *path, void *buf, fuse_fill_dir_t filler,
			 off_t offset, struct fuse_file_info *fi)
{
	(void) offset;
	(void) fi;
	int return_value = 0;
	int dir_position;
	int i = 0;
	char buffer [50];
	cs1550_directory_entry current_dir;
	
	char directory[MAX_FILENAME + 1], filename[MAX_FILENAME + 1], extension[MAX_EXTENSION + 1];
	memset(directory, 0, MAX_FILENAME + 1);
	memset(filename, 0, MAX_FILENAME + 1);
	memset(extension, 0, MAX_EXTENSION + 1);
	
	if (strcmp(path, "/") != 0) { // If this is not just the root path
		sscanf(path, "/%[^/]/%[^.].%s", directory, filename, extension);
		dir_position = find_directory(directory);
		if ((directory != NULL) && (dir_position != -1)) {
			get_directory(&current_dir, dir_position);
			filler(buf, ".", NULL, 0);
			filler(buf, "..", NULL, 0);
			
			for(; i< current_dir.nFiles; i++) {
				if(strlen(current_dir.files[i].fext) > 0) {
					sprintf(buffer, "%s.%s", current_dir.files[i].fname, current_dir.files[i].fext);
				}
				else {
					sprintf(buffer, "%s", current_dir.files[i].fname);
				}
				filler(buf, buffer, NULL, 0);
			}
			
			return_value = 0;
		}
		else {
			return_value = -ENOENT;
		}
	}
	else { // If this is just the root of the mount
		i = 0;
		filler(buf, ".", NULL,0);
		filler(buf, "..", NULL, 0);
		while(get_directory(&current_dir, i) != 0) {
			filler(buf, current_dir.dname, NULL, 0);
			i++;
		}
		return_value = 0;
	}
	
	return 0;
}

/* 
 * Creates a directory. We can ignore mode since we're not dealing with
 * permissions, as long as getattr returns appropriate ones for us.
 * 
 * Requires:
 * path for the directory
 * mode (not used)
 * 
 * Returns:
 * Negative values on failure/error
 * 0 on success
 *  
 */
static int cs1550_mkdir(const char *path, mode_t mode)
{
	(void) path;
	(void) mode;
	char directory[MAX_FILENAME + 1], filename[MAX_FILENAME + 1], extension[MAX_EXTENSION + 1];
	int return_value = 0;
	cs1550_directory_entry temp_directory;
	
	sscanf(path, "/%[^/]/%[^.].%s", directory, filename, extension);
	if (directory == NULL || directory[0] == '\0') { // If no directory was specified
		return_value = -EPERM;
	}
	else {
		if (find_directory(directory) == -1) { // If the directory does not already exist
            if (strlen(directory) <= MAX_FILENAME) { // If the filename is not too long
               
				memset(&temp_directory, 0, sizeof(struct cs1550_directory_entry));
				strcpy(temp_directory.dname, directory);
				temp_directory.nFiles = 0;
				
				FILE *f = fopen(".directories", "a");
				fwrite(&temp_directory, sizeof(cs1550_directory_entry),1,f);
				fclose(f);
            }
            else {
                return_value = -ENAMETOOLONG;   // The filename is too long
            }
        }
        else {
            return_value = -EEXIST; // directory already exists
        }
	}
	
    return return_value;
}

/* 
 * Removes a directory.
 */
static int cs1550_rmdir(const char *path)
{
	(void) path;
    return 0;
}

/* cs1550_mknod
 * Does the actual creation of a file. Mode and dev can be ignored.
 * 
 * Requires:
 * path
 * mode (not implemented here)
 * dev (not implemented here)
 * 
 * Returns:
 * 0 on success
 * -ENAMETOOLONG if the name is beyond 8.3 chars
 * -EPERM if the file is trying to be created in the root dir
 * -EEXIST if the file already exists
 * 
 */
static int cs1550_mknod(const char *path, mode_t mode, dev_t dev)
{
	(void) mode;
	(void) dev;
	
	char directory[MAX_FILENAME + 1], filename[MAX_FILENAME + 1], extension[MAX_EXTENSION + 1];
	int return_value = 0;
	cs1550_directory_entry temp_directory;
	int dir_position, dir_return, file_return;
	
	memset(directory, 0, MAX_FILENAME + 1);
	memset(filename, 0, MAX_FILENAME + 1);
	memset(extension, 0, MAX_EXTENSION + 1);
	
	sscanf(path, "/%[^/]/%[^.].%s", directory, filename, extension);
	if (directory != NULL) {
		if (strlen(filename) < MAX_FILENAME) {
			if ((extension == NULL) || (extension[0] == '\0') || ((extension != NULL && extension[0] != '\0') && (strlen(extension) <= MAX_EXTENSION))) { // If the extension is either NULL, or if it is not NULL but less than the max length
				dir_position = find_directory(directory);
				if (dir_position == -1) {
					return_value = -1; // Problem finding the current directory
				}
				
				dir_return = get_directory(&temp_directory, dir_position);
				if (dir_return == 0) {
					return_value = -1; // Problem getting the current directory
				}
				
				file_return = find_file(&temp_directory, filename, extension);
				if (file_return == -1) { // If the file does not already exist
					strcpy(temp_directory.files[temp_directory.nFiles].fname, filename);
					if(extension != NULL) {
						strcpy(temp_directory.files[temp_directory.nFiles].fext, extension);
					}
					else {
						strcpy(temp_directory.files[temp_directory.nFiles].fext, "\0");
					}
					
					temp_directory.files[temp_directory.nFiles].nStartBlock = allocate_block();
					if (temp_directory.files[temp_directory.nFiles].nStartBlock == -1) {
						return -1; // No space?
					}
					temp_directory.files[temp_directory.nFiles].fsize = 0;
					temp_directory.nFiles += 1;
					
					update_directory(&temp_directory, dir_position); // Write the updated directory to the .directories file
					return_value = 0;
				
				}
				else {
					return_value = -EEXIST; // File already exists
				}
			}
			else {
				return_value = -ENAMETOOLONG; // The extension is too long
			}
		}
		else {
			return_value = -ENAMETOOLONG; // The filename is too long
		}
	}
	else { // Trying to create file in root directory
		return_value = -EPERM;
	}
	//Need to do:
	// Find a free block
	// Create file inside block
	// Create file inside of directory
	
	return return_value;
}

/*
 * Deletes a file
 */
static int cs1550_unlink(const char *path)
{
    (void) path;

    return 0;
}

/* 
 * Read size bytes from file into buf starting from offset
 *
 */
static int cs1550_read(const char *path, char *buf, size_t size, off_t offset,
			  struct fuse_file_info *fi)
{
	(void) buf;
	(void) offset;
	(void) fi;
	(void) path;
	int return_value = 0;
	int temp_offset = offset; // Make a copy
	int dir_position, dir_return, file_return, read_return, buffer_return, data_to_read, block_num;
	
	char directory[MAX_FILENAME + 1], filename[MAX_FILENAME + 1], extension[MAX_EXTENSION + 1];
	cs1550_directory_entry temp_directory;
	cs1550_disk_block temp_block;
	
	if (offset > size || size <= 0) { // If the offset is past the file size, just return -1
		return -1;
	}
	
	data_to_read = size - offset; // How much data needs to be read from the block(s)
	
	memset(directory, 0, MAX_FILENAME + 1);
	memset(filename, 0, MAX_FILENAME + 1);
	memset(extension, 0, MAX_EXTENSION + 1);
	
	sscanf(path, "/%[^/]/%[^.].%s", directory, filename, extension);
	if (directory != NULL) {
		if (strlen(filename) < MAX_FILENAME) {
			if ((extension == NULL) || (extension[0] == '\0') || ((extension != NULL && extension[0] != '\0') && (strlen(extension) <= MAX_EXTENSION))) { // If the extension is either NULL, or if it is not NULL but less than the max length
				dir_position = find_directory(directory);
				if (dir_position == -1) {
					return_value = -1; // Problem finding the current directory
				}
				
				dir_return = get_directory(&temp_directory, dir_position);
				if (dir_return == 0) {
					return_value = -1; // Problem getting the current directory
				}
				
				file_return = find_file(&temp_directory, filename, extension);
				if (file_return != -1) { // If the file exists
					
					if (temp_directory.files[file_return].fsize == 0) { // If it is an empty file, just return now
						return 0;
					}
					
					block_num = temp_directory.files[file_return].nStartBlock;
					
					while( offset < size) {
						
						read_return = read_block(&temp_block, block_num);
						if (temp_offset > MAX_DATA_IN_BLOCK) {
							block_num = temp_block.nNextBlock;
							temp_offset -= MAX_DATA_IN_BLOCK;
							continue;
						}
						else {
							buffer_return = block_to_buffer(&temp_block, buf, temp_offset, size - offset);
							temp_offset = 0;
							if (buffer_return == 0) {
								break;
							}
							else {
								block_num = temp_block.nNextBlock;
								offset += MAX_DATA_IN_BLOCK;
								buf += MAX_DATA_IN_BLOCK; // Maybe?
							}
						}
						
					}
					
					return_value = size;				
				}
				else {
					return_value = -1; // File already exists
				}
			}
			else {
				return_value = -1; // The extension is too long
			}
		}
		else {
			return_value = -1; // The filename is too long
		}
	}
	else {
		return_value = -1;
	}

	return return_value;
}

/* 
 * Write size bytes from buf into file starting from offset
 *
 */
static int cs1550_write(const char *path, const char *buf, size_t size, 
			  off_t offset, struct fuse_file_info *fi)
{
	(void) buf;
	(void) offset;
	(void) fi;
	(void) path;
	int dir_position, dir_return, file_return, read_return, write_return, buffer_return, data_to_read, block_num;
	int temp_offset = offset; // Make a copy
	int return_value = 0;
	
	char directory[MAX_FILENAME + 1], filename[MAX_FILENAME + 1], extension[MAX_EXTENSION + 1];
	cs1550_directory_entry temp_directory;
	cs1550_disk_block temp_block;
	
	memset(&temp_block, 0, sizeof(cs1550_disk_block)); // Clear the temporary block
	
	if ((offset > size) || (size <= 0)) { // If trying to write beyond the end of the file or write nothing
		return -1;
	}
	
	data_to_read = size - offset;
	
	memset(directory, 0, MAX_FILENAME + 1);
	memset(filename, 0, MAX_FILENAME + 1);
	memset(extension, 0, MAX_EXTENSION + 1);
	
	sscanf(path, "/%[^/]/%[^.].%s", directory, filename, extension);
	if (directory != NULL) {
		if ((filename != NULL) && (filename[0] != '\0') && (strlen(filename) < MAX_FILENAME)) {
			if ((extension == NULL) || (extension[0] == '\0') || ((extension != NULL && extension[0] != '\0') && (strlen(extension) <= MAX_EXTENSION))) { // If the extension is either NULL, or if it is not NULL but less than the max length
				dir_position = find_directory(directory);
				if (dir_position == -1) {
					return_value = -1; // Problem finding the current directory
				}
				dir_return = get_directory(&temp_directory, dir_position);
				if (dir_return == 0) {
					return_value = -1; // Problem getting the current directory
				}
				
				file_return = find_file(&temp_directory, filename, extension);
				if (file_return != -1) { // If the file exists
					if (size == 0) { // If it is an empty file, just return now
						return 0;
					}
					
					block_num = temp_directory.files[file_return].nStartBlock;
					temp_directory.files[file_return].fsize = size;
					update_directory(&temp_directory, dir_position); // Write the updated directory to the .directories file
					
					while( temp_offset >= MAX_DATA_IN_BLOCK) {
						block_num = temp_block.nNextBlock;
						temp_offset -= MAX_DATA_IN_BLOCK;
						read_return = read_block(&temp_block, block_num);
					}
					
					while( offset < size) {
						if (temp_offset > MAX_DATA_IN_BLOCK) {
							block_num = temp_block.nNextBlock;
							temp_offset -= MAX_DATA_IN_BLOCK;
							continue;
						}
						else {
							buffer_return = buffer_to_block(&temp_block, buf, temp_offset, size - offset);
							if (buffer_return != 0 && (temp_block.nNextBlock <= 0)) { // If another block is needed
								temp_block.nNextBlock = allocate_block(); // Allocate another block
							}
							write_return = write_block(&temp_block, block_num);
							temp_offset = 0;
							
							if (buffer_return == 0) {
								break;
							}
							else {
								block_num = temp_block.nNextBlock;
								offset += MAX_DATA_IN_BLOCK;
								read_return = read_block(&temp_block, block_num);
								buf += MAX_DATA_IN_BLOCK;
							}
						}
					}
					
					return_value = size;				
				}
				else {
					return_value = -1; // File already exists
				}
			}
			else {
				return_value = -1; // The extension is too long
			}
		}
		else {
			return_value = -EISDIR; // Just a directory, not a file
		}
	}
	else {
		return_value = -1;
	}
	
	return return_value;
}

/******************************************************************************
 *
 *  DO NOT MODIFY ANYTHING BELOW THIS LINE
 *
 *****************************************************************************/

/*
 * truncate is called when a new file is created (with a 0 size) or when an
 * existing file is made shorter. We're not handling deleting files or 
 * truncating existing ones, so all we need to do here is to initialize
 * the appropriate directory entry.
 *
 */
static int cs1550_truncate(const char *path, off_t size)
{
	(void) path;
	(void) size;

    return 0;
}


/* 
 * Called when we open a file
 *
 */
static int cs1550_open(const char *path, struct fuse_file_info *fi)
{
	(void) path;
	(void) fi;
    /*
        //if we can't find the desired file, return an error
        return -ENOENT;
    */

    //It's not really necessary for this project to anything in open

    /* We're not going to worry about permissions for this project, but 
	   if we were and we don't have them to the file we should return an error

        return -EACCES;
    */

    return 0; //success!
}

/*
 * Called when close is called on a file descriptor, but because it might
 * have been dup'ed, this isn't a guarantee we won't ever need the file 
 * again. For us, return success simply to avoid the unimplemented error
 * in the debug log.
 */
static int cs1550_flush (const char *path , struct fuse_file_info *fi)
{
	(void) path;
	(void) fi;

	return 0; //success!
}

//register our new functions as the implementations of the syscalls
static struct fuse_operations hello_oper = {
    .getattr	= cs1550_getattr,
    .readdir	= cs1550_readdir,
    .mkdir	= cs1550_mkdir,
	.rmdir = cs1550_rmdir,
    .read	= cs1550_read,
    .write	= cs1550_write,
	.mknod	= cs1550_mknod,
	.unlink = cs1550_unlink,
	.truncate = cs1550_truncate,
	.flush = cs1550_flush,
	.open	= cs1550_open,
};

//Don't change this.
int main(int argc, char *argv[])
{
	return fuse_main(argc, argv, &hello_oper, NULL);
}
