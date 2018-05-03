/*
 Navicat Premium Data Transfer

 Source Server         : Earnit-DEV-ROOT
 Source Server Type    : PostgreSQL
 Source Server Version : 90602
 Source Host           : earnit-dev-db.cxyztbjharwv.us-west-2.rds.amazonaws.com
 Source Database       : earnit
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 90602
 File Encoding         : utf-8

 Date: 08/01/2017 08:33:06 AM
*/

-- ----------------------------
--  Sequence structure for accounts_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."accounts_id_seq";
CREATE SEQUENCE "public"."accounts_id_seq" INCREMENT 1 START 2 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."accounts_id_seq" OWNER TO "root";

-- ----------------------------
--  Sequence structure for childrens_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."childrens_id_seq";
CREATE SEQUENCE "public"."childrens_id_seq" INCREMENT 1 START 6 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."childrens_id_seq" OWNER TO "root";

-- ----------------------------
--  Sequence structure for goals_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."goals_id_seq";
CREATE SEQUENCE "public"."goals_id_seq" INCREMENT 1 START 2 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."goals_id_seq" OWNER TO "root";

-- ----------------------------
--  Sequence structure for hibernate_sequence
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."hibernate_sequence";
CREATE SEQUENCE "public"."hibernate_sequence" INCREMENT 50 START 501 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."hibernate_sequence" OWNER TO "root";

-- ----------------------------
--  Sequence structure for parents_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."parents_id_seq";
CREATE SEQUENCE "public"."parents_id_seq" INCREMENT 1 START 6 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."parents_id_seq" OWNER TO "root";

-- ----------------------------
--  Sequence structure for repitition_schedule_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."repitition_schedule_id_seq";
CREATE SEQUENCE "public"."repitition_schedule_id_seq" INCREMENT 1 START 11 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."repitition_schedule_id_seq" OWNER TO "root";

-- ----------------------------
--  Sequence structure for task_comments_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."task_comments_id_seq";
CREATE SEQUENCE "public"."task_comments_id_seq" INCREMENT 1 START 2 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."task_comments_id_seq" OWNER TO "root";

-- ----------------------------
--  Sequence structure for tasks_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."tasks_id_seq";
CREATE SEQUENCE "public"."tasks_id_seq" INCREMENT 1 START 14 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."tasks_id_seq" OWNER TO "root";

-- ----------------------------
--  Table structure for tasks
-- ----------------------------
DROP TABLE IF EXISTS "public"."tasks";
CREATE TABLE "public"."tasks" (
	"id" int4 NOT NULL DEFAULT nextval('tasks_id_seq'::regclass),
	"goal_id" int4,
	"child_id" int4 NOT NULL,
	"name" varchar(512) NOT NULL COLLATE "default",
	"due_date" timestamp(6) NOT NULL,
	"allowance" float8 NOT NULL,
	"status" varchar(255) COLLATE "default",
	"picture_required" int4 DEFAULT 0,
	"create_date" timestamp(6) NOT NULL,
	"update_date" timestamp(6) NULL,
	"repetition_schedule_id" int4,
	"description" varchar(500) COLLATE "default"
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."tasks" OWNER TO "root";

-- ----------------------------
--  Table structure for repitition_schedule
-- ----------------------------
DROP TABLE IF EXISTS "public"."repitition_schedule";
CREATE TABLE "public"."repitition_schedule" (
	"id" int4 NOT NULL DEFAULT nextval('repitition_schedule_id_seq'::regclass),
	"repeat" varchar(100) NOT NULL COLLATE "default",
	"expiry_date" timestamp(6) NULL
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."repitition_schedule" OWNER TO "root";

-- ----------------------------
--  Table structure for parents
-- ----------------------------
DROP TABLE IF EXISTS "public"."parents";
CREATE TABLE "public"."parents" (
	"id" int4 NOT NULL DEFAULT nextval('parents_id_seq'::regclass),
	"account_id" int4 NOT NULL,
	"first_name" varchar(100) NOT NULL COLLATE "default",
	"last_name" varchar(100) DEFAULT NULL::bpchar COLLATE "default",
	"email" varchar(255) NOT NULL COLLATE "default",
	"password" varchar(255) NOT NULL COLLATE "default",
	"avatar" varchar(500) DEFAULT NULL::bpchar COLLATE "default",
	"phone" varchar(100) DEFAULT NULL::bpchar COLLATE "default",
	"create_date" timestamp(6) NOT NULL,
	"update_date" timestamp(6) NULL
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."parents" OWNER TO "root";

-- ----------------------------
--  Table structure for task_comments
-- ----------------------------
DROP TABLE IF EXISTS "public"."task_comments";
CREATE TABLE "public"."task_comments" (
	"id" int4 NOT NULL DEFAULT nextval('task_comments_id_seq'::regclass),
	"task_id" int4 NOT NULL,
	"comment" text NOT NULL COLLATE "default",
	"picture_url" varchar(512) DEFAULT NULL::bpchar COLLATE "default",
	"create_date" timestamp(6) NOT NULL,
	"update_date" timestamp(6) NULL,
	"read_status" int4 NOT NULL DEFAULT 0
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."task_comments" OWNER TO "root";

-- ----------------------------
--  Table structure for childrens
-- ----------------------------
DROP TABLE IF EXISTS "public"."childrens";
CREATE TABLE "public"."childrens" (
	"id" int4 NOT NULL DEFAULT nextval('childrens_id_seq'::regclass),
	"account_id" int4 NOT NULL,
	"first_name" varchar(100) NOT NULL COLLATE "default",
	"last_name" varchar(100) DEFAULT NULL::bpchar COLLATE "default",
	"email" varchar(255) NOT NULL COLLATE "default",
	"password" varchar(255) NOT NULL COLLATE "default",
	"avatar" varchar(500) DEFAULT NULL::bpchar COLLATE "default",
	"phone" varchar(100) DEFAULT NULL::bpchar COLLATE "default",
	"create_date" timestamp(6) NOT NULL,
	"update_date" timestamp(6) NULL,
	"message" text COLLATE "default"
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."childrens" OWNER TO "root";

-- ----------------------------
--  Table structure for goals
-- ----------------------------
DROP TABLE IF EXISTS "public"."goals";
CREATE TABLE "public"."goals" (
	"id" int4 NOT NULL DEFAULT nextval('goals_id_seq'::regclass),
	"child_id" int4 NOT NULL,
	"name" varchar(500) NOT NULL COLLATE "default",
	"amount" float8 NOT NULL,
	"create_date" timestamp(6) NOT NULL,
	"update_date" timestamp(6) NULL
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."goals" OWNER TO "root";

-- ----------------------------
--  Table structure for accounts
-- ----------------------------
DROP TABLE IF EXISTS "public"."accounts";
CREATE TABLE "public"."accounts" (
	"id" int4 NOT NULL DEFAULT nextval('accounts_id_seq'::regclass),
	"account_code" varchar(50) NOT NULL COLLATE "default",
	"create_date" timestamp(6) NOT NULL
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."accounts" OWNER TO "root";


-- ----------------------------
--  Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."accounts_id_seq" RESTART 3 OWNED BY "accounts"."id";
ALTER SEQUENCE "public"."childrens_id_seq" RESTART 7 OWNED BY "childrens"."id";
ALTER SEQUENCE "public"."goals_id_seq" RESTART 3 OWNED BY "goals"."id";
ALTER SEQUENCE "public"."hibernate_sequence" RESTART 551;
ALTER SEQUENCE "public"."parents_id_seq" RESTART 7 OWNED BY "parents"."id";
ALTER SEQUENCE "public"."repitition_schedule_id_seq" RESTART 12 OWNED BY "repitition_schedule"."id";
ALTER SEQUENCE "public"."task_comments_id_seq" RESTART 3 OWNED BY "task_comments"."id";
ALTER SEQUENCE "public"."tasks_id_seq" RESTART 15 OWNED BY "tasks"."id";
-- ----------------------------
--  Primary key structure for table tasks
-- ----------------------------
ALTER TABLE "public"."tasks" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Primary key structure for table repitition_schedule
-- ----------------------------
ALTER TABLE "public"."repitition_schedule" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Primary key structure for table parents
-- ----------------------------
ALTER TABLE "public"."parents" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Primary key structure for table task_comments
-- ----------------------------
ALTER TABLE "public"."task_comments" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Primary key structure for table childrens
-- ----------------------------
ALTER TABLE "public"."childrens" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Indexes structure for table childrens
-- ----------------------------
CREATE UNIQUE INDEX  "childrens_id_key" ON "public"."childrens" USING btree("id" "pg_catalog"."int4_ops" ASC NULLS LAST);

-- ----------------------------
--  Primary key structure for table goals
-- ----------------------------
ALTER TABLE "public"."goals" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Primary key structure for table accounts
-- ----------------------------
ALTER TABLE "public"."accounts" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Foreign keys structure for table tasks
-- ----------------------------
ALTER TABLE "public"."tasks" ADD CONSTRAINT "fkacjm73w3vfy4x7gj42hd1ltkb" FOREIGN KEY ("goal_id") REFERENCES "public"."goals" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE "public"."tasks" ADD CONSTRAINT "fk_task_goal" FOREIGN KEY ("goal_id") REFERENCES "public"."goals" ("id") ON UPDATE CASCADE ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE "public"."tasks" ADD CONSTRAINT "fkpu24d8ujrar5rlcvgr1jsxdkb" FOREIGN KEY ("repetition_schedule_id") REFERENCES "public"."repitition_schedule" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE "public"."tasks" ADD CONSTRAINT "fk_repition_schedule" FOREIGN KEY ("repetition_schedule_id") REFERENCES "public"."repitition_schedule" ("id") ON UPDATE CASCADE ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE "public"."tasks" ADD CONSTRAINT "fk1ydr7dp54e2qok35d7kgrj78b" FOREIGN KEY ("child_id") REFERENCES "public"."childrens" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE "public"."tasks" ADD CONSTRAINT "fk_task_child" FOREIGN KEY ("child_id") REFERENCES "public"."childrens" ("id") ON UPDATE CASCADE ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Foreign keys structure for table parents
-- ----------------------------
ALTER TABLE "public"."parents" ADD CONSTRAINT "fk3q3tbhasckmps13tab9d7is32" FOREIGN KEY ("account_id") REFERENCES "public"."accounts" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE "public"."parents" ADD CONSTRAINT "fk_parents_accounts" FOREIGN KEY ("account_id") REFERENCES "public"."accounts" ("id") ON UPDATE CASCADE ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Foreign keys structure for table task_comments
-- ----------------------------
ALTER TABLE "public"."task_comments" ADD CONSTRAINT "fk9517viwn2geh1gpivj6l9y64u" FOREIGN KEY ("task_id") REFERENCES "public"."tasks" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE "public"."task_comments" ADD CONSTRAINT "fk_task_comment_task" FOREIGN KEY ("task_id") REFERENCES "public"."tasks" ("id") ON UPDATE CASCADE ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Foreign keys structure for table childrens
-- ----------------------------
ALTER TABLE "public"."childrens" ADD CONSTRAINT "fkhosehnl68r5xcd5mokmiwvppr" FOREIGN KEY ("account_id") REFERENCES "public"."accounts" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE "public"."childrens" ADD CONSTRAINT "fk_childrens_accounts_1" FOREIGN KEY ("account_id") REFERENCES "public"."accounts" ("id") ON UPDATE CASCADE ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Foreign keys structure for table goals
-- ----------------------------
ALTER TABLE "public"."goals" ADD CONSTRAINT "fk5ppdqf5t544h68hk4n8pu11tp" FOREIGN KEY ("child_id") REFERENCES "public"."childrens" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE "public"."goals" ADD CONSTRAINT "fk_goal_child" FOREIGN KEY ("child_id") REFERENCES "public"."childrens" ("id") ON UPDATE CASCADE ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE "public"."goals" ADD CONSTRAINT "fk36sa9uc3vkysp86yi8gyu8cxw" FOREIGN KEY ("child_id") REFERENCES "public"."childrens" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

